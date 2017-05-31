package com.x2mobile.wodjar.ui.adapter.base

import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    private var items: MutableList<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    protected fun getItems(): MutableList<T>? {
        return items
    }

    fun getItem(position: Int): T {
        return items!![position]
    }

    fun getItemPosition(item: T): Int {
        return items!!.indexOf(item)
    }

    open fun setItems(items: MutableList<T>?) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addItem(item: T, position: Int) {
        items!!.add(position, item)
        notifyItemInserted(position)
    }

    fun addItems(items: Collection<T>, position: Int) {
        this.items!!.addAll(position, items)
        notifyItemRangeChanged(position, items.size)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(items!!, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItem(item: T) {
        val position = getItemPosition(item)
        if (position >= 0) {
            removeItem(position)
        }
    }

    fun removeItem(position: Int) {
        items!!.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun clearItems() {
        items!!.clear()
        notifyDataSetChanged()
    }

}

abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal abstract fun bindData(item: T)
}
