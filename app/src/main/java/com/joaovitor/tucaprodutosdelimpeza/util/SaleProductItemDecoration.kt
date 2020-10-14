package com.joaovitor.tucaprodutosdelimpeza.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.joaovitor.tucaprodutosdelimpeza.R

class SaleProductItemDecoration(ctx: Context) :
    ItemDecoration() {
    private val paint: Paint = Paint()
    private val context: Context
    private val dividerHeight: Int
    private var layoutOrientation = -1
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[dividerHeight, dividerHeight, dividerHeight] = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager is LinearLayoutManager && layoutOrientation == -1) {
            layoutOrientation = (parent.layoutManager as LinearLayoutManager?)!!.orientation
        }
        if (layoutOrientation == LinearLayoutManager.HORIZONTAL) {
            horizontal(c, parent)
        } else {
            vertical(c, parent)
        }
    }

    private fun horizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val itemCount = parent.childCount
        for (i in 0 until itemCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = child.left + dividerHeight
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    private fun vertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = child.top + dividerHeight
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    init {
        paint.color = ContextCompat.getColor(ctx, R.color.colorGray)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3.toFloat()
        dividerHeight = 0
        context = ctx
    }
}