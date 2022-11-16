package ru.technostore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_homestore.view.*
import ru.technostore.R
import ru.technostore.model.HomeStore

class HomeStoreAdapter(var onHomeStoreClickListener: OnHomeStoreClickListener) : ListAdapter<HomeStore, HomeStoreAdapter.HomeStoreVH>(HomeStoreDiffUtil()) {


    inner class HomeStoreVH(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
        fun onBind(homeStore: HomeStore) {

            view.setOnClickListener { onHomeStoreClickListener.onHomeStoreClick(homeStore) }

            view.tv_subtitle.text = homeStore.subtitle
            view.tv_title.text = homeStore.title
            Glide.with(context).load(homeStore.picture).into(view.iv_picture)
            view.b_isBuy.isVisible = homeStore.isBuy == true
            if (homeStore.isNew == true) {
                view.ll_isNew.isVisible = true
                view.tv_isNew.isVisible = true
            } else {
                view.ll_isNew.isVisible = false
                view.tv_isNew.isVisible = false
            }
        }
    }

    class HomeStoreDiffUtil : DiffUtil.ItemCallback<HomeStore>() {
        override fun areItemsTheSame(oldItem: HomeStore, newItem: HomeStore): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeStore, newItem: HomeStore): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeStoreVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_homestore, parent, false)
        val lp = view.layoutParams
        lp.width = parent.measuredWidth // width is 1/3 of the width of the RecyclerView
        return HomeStoreVH(view, parent.context)
    }

    override fun onBindViewHolder(holder: HomeStoreVH, position: Int) {
        val homeStore = getItem(position)
        holder.onBind(homeStore)

    }
    interface OnHomeStoreClickListener {
        fun onHomeStoreClick(homeStore: HomeStore)
    }
}