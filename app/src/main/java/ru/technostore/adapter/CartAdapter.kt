package ru.technostore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.technostore.databinding.ItemCartLayoutBinding
import ru.technostore.model.BasketItem

class CartAdapter : ListAdapter<BasketItem, CartAdapter.MyCartVh>(CartDiffUtil()) {

    inner class MyCartVh(var binding: ItemCartLayoutBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(basket: BasketItem) {
            Glide.with(context).load(basket.images).into(binding.ivImages)
            binding.tvTitle.text = basket.title
            binding.tvPrice.text = "$${basket.price}.00"

        }
    }

    class CartDiffUtil : DiffUtil.ItemCallback<BasketItem>() {
        override fun areItemsTheSame(oldItem: BasketItem, newItem: BasketItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BasketItem, newItem: BasketItem): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartVh {
        return MyCartVh(ItemCartLayoutBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context)
    }

    override fun onBindViewHolder(holder: MyCartVh, position: Int) {
        val basket = getItem(position)
        holder.onBind(basket)
    }
}