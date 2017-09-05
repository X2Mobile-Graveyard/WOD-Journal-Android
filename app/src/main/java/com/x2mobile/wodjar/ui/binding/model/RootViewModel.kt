package com.x2mobile.wodjar.ui.binding.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.widget.TextView
import com.x2mobile.wodjar.BR

class RootViewModel(title: String, val focusChangeListener: View.OnFocusChangeListener,
                    val editorActionListener: TextView.OnEditorActionListener) : BaseObservable() {

    @Bindable
    var title: String = title
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @Bindable
    var titleChangeStarted: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.titleChangeStarted)
            if (!value) {
                notifyPropertyChanged(BR.title)
            }
        }

    @Bindable
    var titleChangeEnabled: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.titleChangeEnabled)
        }

    fun getEditClickListener(): View.OnClickListener = View.OnClickListener {
        titleChangeStarted = true
    }
}
