package ru.technostore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.technostore.R
import ru.technostore.databinding.ItemBestsellerViewBinding
import ru.technostore.model.BestSeller

class BestSellerAdapter(var onItemBestSellerClickListener: OnItemBestSellerClickListener) :
    ListAdapter<BestSeller, BestSellerAdapter.BestSellerVH>(BestSellerDiffUtil()) {

    inner class BestSellerVH(var binding: ItemBestsellerViewBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(bestSeller: BestSeller) {
            binding.root.setOnClickListener {
                onItemBestSellerClickListener.onItemClick()
            }

            binding.apply {
                if(bestSeller.isFavorites == true) {
                    Glide.with(context).load(R.mipmap.im_is_like2).into(ivIsFavourite)
                }else {
                    Glide.with(context).load(R.mipmap.im_is_like).into(ivIsFavourite)
                }
                tvTitle.text = bestSeller.title
                tvDiscountPrice.text = bestSeller.discountPrice.toString()
                tvPriceWithoutDiscount.text = bestSeller.priceWithoutDiscount.toString()
                Glide.with(context).load(bestSeller.picture).into(ivPicture)
            }
        }
    }

    class BestSellerDiffUtil : DiffUtil.ItemCallback<BestSeller>() {
        override fun areItemsTheSame(oldItem: BestSeller, newItem: BestSeller): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: BestSeller, newItem: BestSeller): Boolean =
            oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellerVH {
        return BestSellerVH(ItemBestsellerViewBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context)
    }

    override fun onBindViewHolder(holder: BestSellerVH, position: Int) {
        val bestSeller = getItem(position)
        holder.onBind(bestSeller)
    }

    interface OnItemBestSellerClickListener {
        fun onItemClick()
    }
}