package ru.technostore.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(private var margin: Int, private val columns: Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // Apply offset only to first item
        val position = parent.getChildLayoutPosition(view)
        // set right margin to all
        if(position == 0 || position == 1 || position == 2) {
            outRect.right += margin
            // set left margin to all
            //outRect.left += margin
        }
    }
}