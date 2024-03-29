package com.x2mobile.wodjar.ui.binding.adapter

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.text.TextUtils
import android.widget.TextView

object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: TextView, float: Float) {
        if (float == 0f) {
            view.text = ""
            return
        }
        view.text = float.toString()
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: TextView, int: Int) {
        if (int == 0) {
            view.text = ""
            return
        }
        view.text = int.toString()
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getFloat(view: TextView): Float {
        val text = view.text.toString()
        if (TextUtils.isEmpty(text)) {
            return 0f
        }
        return try {
            text.toFloat()
        } catch (exception: NumberFormatException) {
            0.0f
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getInt(view: TextView): Int {
        val text = view.text.toString()
        if (TextUtils.isEmpty(text)) {
            return 0
        }
        return try {
            text.toInt()
        } catch (exception: NumberFormatException) {
            0
        }
    }
}
