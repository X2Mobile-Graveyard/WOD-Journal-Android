package com.x2mobile.wodjar.ui.adapter.base

import android.text.TextUtils
import com.x2mobile.wodjar.data.model.base.Filterable

abstract class FilterableBaseAdapter<T : Filterable, VH : BaseViewHolder<T>> : BaseAdapter<T, VH>() {

    private var originalItems: MutableList<T>? = null

    fun filter(query: String) {
        if (TextUtils.isEmpty(query)) {
            setItems(originalItems!!)
            originalItems = null
        } else {
            if (originalItems == null) {
                originalItems = getItems()
            }
            setItems(originalItems!!.filter { it.matches(query) }.toMutableList())
        }
    }

}