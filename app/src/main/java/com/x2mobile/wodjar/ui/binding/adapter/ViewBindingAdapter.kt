package com.x2mobile.wodjar.ui.binding.adapter

import android.databinding.BindingAdapter
import android.view.View

class ViewBindingAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter("android:onFocus")
        fun setOnFocusChangeListener(view: View, focusChangeListener: View.OnFocusChangeListener) {
            view.onFocusChangeListener = focusChangeListener
        }

        @JvmStatic
        @BindingAdapter("android:focus")
        fun setFocus(view: View, focus: Boolean) {
            if (focus) {
                view.post { view.requestFocus() }
            }
        }
    }

}
