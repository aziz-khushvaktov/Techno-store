package ru.technostore.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.util.ExceptionPassthroughInputStream
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ru.technostore.MainActivity
import ru.technostore.R
import ru.technostore.adapter.BestSellerAdapter
import ru.technostore.adapter.CategoryAdapter
import ru.technostore.adapter.HomeStoreAdapter
import ru.technostore.databinding.FragmentHomeBinding
import ru.technostore.databinding.ItemCategoryViewBinding
import ru.technostore.model.Category
import ru.technostore.model.HomeStore
import ru.technostore.utils.ConnectivityReceiver
import ru.technostore.utils.PaddingItemDecoration
import ru.technostore.utils.Utils
import ru.technostore.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var categoryAdapter : CategoryAdapter
    private lateinit var bestSellerAdapter : BestSellerAdapter
    private lateinit var homeStoreAdapter: HomeStoreAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        initViews()
        return binding.root
    }

    private fun initViews() {

        setUpCategoryRecycler()

        setUpBestSellerRecycler()

        setUpHomeStoreRecycler()

        binding.llQrCode.setOnClickListener {
           manageBottomSheetDialog()
        }

        binding.tvSeeMore.setOnClickListener { Utils.inProcess(requireContext()) }
        binding.tvSeeMore2.setOnClickListener { Utils.inProcess(requireContext()) }
        binding.tvViewAll.setOnClickListener { Utils.inProcess(requireContext()) }
    }
    private fun getAllCategories(): ArrayList<Category> {
        val categories = ArrayList<Category>()
        categories.add(Category(R.drawable.ic_baseline_smartphone_black,
            resources.getString(R.string.str_phones)))
        categories.add(Category(R.mipmap.ic_computer, resources.getString(R.string.str_computer)))
        categories.add(Category(R.mipmap.ic_health, resources.getString(R.string.str_health)))
        categories.add(Category(R.mipmap.ic_books, resources.getString(R.string.str_books)))
        categories.add(Category(R.drawable.ic_baseline_headset_24,
            resources.getString(R.string.str_gadgets)))

        return categories
    }

    private fun setUpCategoryRecycler() {

        val sdk = android.os.Build.VERSION.SDK_INT

        categoryAdapter = CategoryAdapter(object : CategoryAdapter.OnCategoryItemClickListener{
            override fun onCategoryItemClick(
                category: Category,
                view: ItemCategoryViewBinding,
                position: Int,
            ) {
                if(position == 0) {
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        view.llCircle.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.view_circle_brown))
                        Glide.with(requireContext()).load(R.drawable.ic_baseline_smartphone_white).into(view.ivIcon)
                        view.tvName.setTextColor(Color.parseColor("#FF6E4E"))
                    }else {
                        view.llCircle.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.view_circle_brown))
                        Glide.with(requireContext()).load(R.drawable.ic_baseline_smartphone_white).into(view.ivIcon)
                        view.tvName.setTextColor(Color.parseColor("#FF6E4E"))
                    }
                }else {
                    binding.rvCategory.adapter = categoryAdapter
                }
            }

        })

        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter.submitList(getAllCategories())
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun setUpBestSellerRecycler() {
        binding.rvBestSeller.layoutManager = GridLayoutManager(requireContext(),2)

        bestSellerAdapter = BestSellerAdapter(object : BestSellerAdapter.OnItemBestSellerClickListener {
            override fun onItemClick() {
                findNavController().navigate(R.id.productDetailsFragment)
            }

        })
        try {
            homeViewModel.apiGetAllMainData()
            homeViewModel.mainRes.observe(requireActivity()) {
                bestSellerAdapter.submitList(it!!.bestSeller)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

        binding.rvBestSeller.adapter = bestSellerAdapter
    }

    private fun setUpHomeStoreRecycler() {
        binding.rvHomeStore.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        homeStoreAdapter = HomeStoreAdapter(object : HomeStoreAdapter.OnHomeStoreClickListener {
            override fun onHomeStoreClick(homeStore: HomeStore) {
                findNavController().navigate(R.id.productDetailsFragment)
            }
        })

        //homeViewModel.apiGetAllMainData()
        homeViewModel.mainRes.observe(requireActivity()) {
            homeStoreAdapter.submitList(it!!.homeStore)
        }


        binding.rvHomeStore.adapter = homeStoreAdapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvHomeStore)

    }

    @SuppressLint("InflateParams")
    private fun manageBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val viewDialog = layoutInflater.inflate(R.layout.item_bottomsheet,null,false)

        val bCancel = viewDialog.findViewById<LinearLayout>(R.id.ll_cancel)
        val bDone = viewDialog.findViewById<Button>(R.id.b_done)

        bCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        bDone.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.setCancelable(false)

        bottomSheetDialog.setContentView(viewDialog)
        bottomSheetDialog.show()
    }

}