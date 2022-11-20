package ru.technostore.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Px
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import dagger.hilt.android.AndroidEntryPoint
import ru.technostore.R
import ru.technostore.adapter.ProductAdapter
import ru.technostore.adapter.VPProductAdapter
import ru.technostore.databinding.FragmentProductDetailsBinding
import ru.technostore.model.Image
import ru.technostore.utils.Utils.inProcess
import ru.technostore.utils.Utils.toast
import ru.technostore.viewmodel.ProductViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val binding by lazy { FragmentProductDetailsBinding.inflate(layoutInflater) }
    private val vpProductAdapter by lazy {
        VPProductAdapter(getTabLayoutTitles(),
            childFragmentManager)
    }

    lateinit var productAdapter: ProductAdapter
    lateinit var snapHelper: SnapHelper
    lateinit var layoutManager: LinearLayoutManager

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        initViews()
        return binding.root
    }

    private fun initViews() {
        setUpDateFromAPI()
        addFragmentToVPAdapter()
        setUpViewPager()


        binding.rvProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        layoutManager = ProminentLayoutManager(requireContext())
        productAdapter = ProductAdapter(listImages())
        snapHelper = PagerSnapHelper()

        binding.rvProduct.adapter = productAdapter

        with(binding.rvProduct) {
            setItemViewCacheSize(4)
            layoutManager = this@ProductDetailsFragment.layoutManager
                productAdapter = this@ProductDetailsFragment.productAdapter

            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())

            snapHelper.attachToRecyclerView(this)
        }


        binding.llBackArrow.setOnClickListener { findNavController().navigate(R.id.homeFragment) }

        binding.llCart.setOnClickListener { findNavController().navigate(R.id.cartFragment) }

        binding.llIsFavourite.setOnClickListener { inProcess(requireContext()) }

    }


    private fun setUpDateFromAPI() {
        try {
            productViewModel.getProductResponseList()
            productViewModel.productResponseList.observe(requireActivity()) {
                binding.apply {
                    tvTitle.text = it!!.title
                    llIsFavourite.isVisible = it.isFavorites == true
                    ratingBar.rating = it.rating!!.toFloat()

                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setUpViewPager() {
        binding.vpProduct.adapter = vpProductAdapter
        binding.tabLProduct.setupWithViewPager(binding.vpProduct)
    }

    private fun listImages(): ArrayList<Image> {
        val list = ArrayList<Image>()
        list.add(Image("https://avatars.mds.yandex.net/get-mpic/5235334/img_id5575010630545284324.png/orig", 1,356, 200))
        list.add(Image("https://www.manualspdf.ru/thumbs/products/l/1260237-samsung-galaxy-note-20-ultra.jpg", 2,356, 200))
        list.add(Image("https://avatars.mds.yandex.net/get-mpic/5235334/img_id5575010630545284324.png/orig", 3,356, 200))

        return list
    }

    private fun getTabLayoutTitles(): ArrayList<String> {
        val titles = ArrayList<String>()

        titles.add(resources.getString(R.string.str_shop))
        titles.add(resources.getString(R.string.str_details))
        titles.add(resources.getString(R.string.features))

        return titles
    }

    private fun addFragmentToVPAdapter() {
        vpProductAdapter.addFragment(ShopFragment())
        vpProductAdapter.addFragment(DetailsFragment())
        vpProductAdapter.addFragment(FeaturesFragment())
    }

}

/** Offset the first and last items to center them */
class BoundsOffsetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // It is crucial to refer to layoutParams.width (view.width is 0 at this time)!
        val itemWidth = view.layoutParams.width
        val offset = (parent.width - itemWidth) / 2

        if (itemPosition == 0) {
            outRect.left = offset
        } else if (itemPosition == state.itemCount - 1) {
            outRect.right = offset
        }
    }
}

/** Works best with a [LinearLayoutManager] in [LinearLayoutManager.HORIZONTAL] orientation */
class LinearHorizontalSpacingDecoration(@Px private val innerSpacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        outRect.left = if (itemPosition == 0) 0 else innerSpacing / 2
        outRect.right = if (itemPosition == state.itemCount - 1) 0 else innerSpacing / 2
    }
}

/**
 * Arranges items so that the central one appears prominent: its neighbors are scaled down.
 * Based on https://stackoverflow.com/a/54516315/2291104
 */
internal class ProminentLayoutManager(
    context: Context,

    /**
     * This value determines where items reach the final (minimum) scale:
     * - 1f is when their center is at the start/end of the RecyclerView
     * - <1f is before their center reaches the start/end of the RecyclerView
     * - >1f is outside the bounds of the RecyclerView
     * */
    private val minScaleDistanceFactor: Float = 1.5f,

    /** The final (minimum) scale for non-prominent items is 1-[scaleDownBy] */
    private val scaleDownBy: Float = 0.5f
) : LinearLayoutManager(context, HORIZONTAL, false) {

    private val prominentThreshold =
        context.resources.getDimensionPixelSize(R.dimen.prominent_threshold)

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleChildren() }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleChildren()
    }

    private fun scaleChildren() {
        val containerCenter = width / 2f

        // Any view further than this threshold will be fully scaled down
        val scaleDistanceThreshold = minScaleDistanceFactor * containerCenter * 2 // This one

        var translationXForward = 0f

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!

            val childCenter = (child.left + child.right) / 2f
            val distanceToCenter = abs(childCenter - containerCenter) // X

            child.isActivated = distanceToCenter < prominentThreshold

            val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f) // X
            val scale = 1f - scaleDownBy * scaleDownAmount

            child.scaleX = scale
            child.scaleY = scale

            val translationDirection = if (childCenter > containerCenter) -1 else 1 // X
            val translationXFromScale = translationDirection * child.width * (1 - scale) / 2f // X
            child.translationX = translationXFromScale + translationXForward

            translationXForward = 0f

            if (translationXFromScale > 0 && i >= 1) {
                // Edit previous child
                getChildAt(i - 1)!!.translationX += 2 * translationXFromScale // X

            } else if (translationXFromScale < 0) {
                // Pass on to next child
                translationXForward = 2 * translationXFromScale // X
            }
        }
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
        // Since we're scaling down items, we need to pre-load more of them offscreen.
        // The value is sort of empirical: the more we scale down, the more extra space we need.
        return (width / (1 - scaleDownBy)).roundToInt()
    }
}