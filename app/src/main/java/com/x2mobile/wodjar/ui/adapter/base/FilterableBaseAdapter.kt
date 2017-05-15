package com.x2mobile.wodjar.ui.adapter.base

import com.x2mobile.wodjar.data.model.base.Filterable

abstract class FilterableBaseAdapter<T : Filterable, VH : BaseViewHolder<T>> : BaseAdapter<T, VH>() {

    private var originalItems: List<T>? = null

    private var query: String = ""

    override fun setItems(items: MutableList<T>?) {
        originalItems = items
        super.setItems(originalItems?.filter { it.matches(query) }?.toMutableList())
    }

    override fun clearItems() {
        super.clearItems()
        originalItems = null
    }

    fun filter(query: String) {
        this.query = query
        super.setItems(originalItems?.filter { it.matches(query) }?.toMutableList())
    }

}
