package ru.technostore.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.technostore.R
import ru.technostore.databinding.ItemProductLayoutBinding
import ru.technostore.model.Image
import kotlin.math.roundToInt

class ProductAdapter(private val images: ArrayList<Image>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var hasInitParentDimensions = false
    private var maxImageWidth: Int = 0
    private var maxImageHeight: Int = 0
    private var maxImageAspectRatio: Float = 1f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        if(!hasInitParentDimensions) {
            maxImageWidth = parent.width - 2 * parent.resources.getDimensionPixelSize(R.dimen.gradient_width)
            maxImageHeight = parent.height
            maxImageAspectRatio = maxImageWidth.toFloat() / maxImageHeight.toFloat()
            hasInitParentDimensions = true
        }

        return ProductViewHolder(OverlayableImageView(parent.context))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val image = images[position]

        // Change aspect ratio
        val imageAspectRatio = image.aspectRatio
        val targetImageWidth: Int = if(imageAspectRatio < maxImageAspectRatio) {
            // Tall image: height = max
            (maxImageHeight * imageAspectRatio).roundToInt()
        }else {
            // Wide image: width = max
            maxImageWidth
        }

        holder.overlayImageView.layoutParams = RecyclerView.LayoutParams(
            targetImageWidth,
            RecyclerView.LayoutParams.MATCH_PARENT
        )

        // Load image
        holder.overlayImageView.image = image

        holder.overlayImageView.setOnClickListener {
            val rv = holder.overlayImageView.parent as RecyclerView
            rv.smoothScrollToCenteredPosition(position)
        }
    }

    override fun getItemCount(): Int = images.size

    class ProductViewHolder(val overlayImageView: OverlayableImageView) : RecyclerView.ViewHolder(overlayImageView) {}
}

private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
            val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
            val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

            return (dxToStart + dxToEnd) / 2
        }
    }

    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

class OverlayableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ItemProductLayoutBinding.inflate(LayoutInflater.from(context), this)

    var image: Image? = null
        set(value) {
            field = value
            value?.let {
                Glide.with(binding.ivImage)
                    .load(it.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(
                        FitCenter(),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.rounded_corners_radius))
                    )
                    .into(binding.ivImage)
            }
        }


    init {
        layoutTransition = LayoutTransition() // android:animateLayoutChanges="true"
        isActivated = false

    }

    override fun setActivated(activated: Boolean) {
        val isChanging = activated != isActivated
        super.setActivated(activated)

    }
}