package com.x2mobile.wodjar.ui.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.x2mobile.wodjar.R

class DeleteTouchHelperCallback(context: Context, val callback: DeleteListener) : ItemTouchHelper.Callback() {

    private var deleteIcon: Drawable? = null

    private val backgroundPaint: Paint = Paint()

    init {
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = Color.RED
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.END)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = callback.onItemRemoved(viewHolder.adapterPosition)

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView

        canvas.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), backgroundPaint)

        if (dX >= deleteIcon!!.intrinsicWidth * 1.5f) {
            val left = itemView.left + (dX - deleteIcon!!.intrinsicWidth) / 2
            val bottom = Math.round((itemView.height - deleteIcon!!.intrinsicHeight) / 2f)
            deleteIcon!!.setBounds(Math.round(left), itemView.top + bottom, Math.round(left) + deleteIcon!!.intrinsicWidth, itemView.bottom - bottom)
            deleteIcon!!.draw(canvas)
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun isLongPressDragEnabled(): Boolean = false
}

interface DeleteListener {
    fun onItemRemoved(position: Int)
}