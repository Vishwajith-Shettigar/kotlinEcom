package com.example.ecomapp.Util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Horizontalitemdecoration(
    private val amount:Int=10
) :RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right=amount
    }
}