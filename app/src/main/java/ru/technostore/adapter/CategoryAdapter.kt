package ru.technostore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import ru.technostore.databinding.ItemCategoryViewBinding
import ru.technostore.model.Category

class CategoryAdapter(var onCategoryItemClickListener: OnCategoryItemClickListener) : ListAdapter<Category, CategoryAdapter.VH>(MyDiffUtil()) {


    inner class VH(var binding: ItemCategoryViewBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category, position: Int) {

            binding.apply {
                Glide.with(context).load(category.icon).into(ivIcon)
                tvName.text = category.name
            }

            binding.root.setOnClickListener {
                onCategoryItemClickListener.onCategoryItemClick(category, binding,position)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemCategoryViewBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.VH, position: Int) {
        val category = getItem(position)
        holder.onBind(category,position)
    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(category: Category, view: ItemCategoryViewBinding, position: Int)
    }
}