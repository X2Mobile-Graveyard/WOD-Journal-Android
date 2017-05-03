package com.x2mobile.wodjar.ui.adapter.base

abstract class FilterableBaseAdapter<T, VH : BaseViewHolder<T>> : BaseAdapter<T, VH>() {

    fun filter(query: String) {

    }

}