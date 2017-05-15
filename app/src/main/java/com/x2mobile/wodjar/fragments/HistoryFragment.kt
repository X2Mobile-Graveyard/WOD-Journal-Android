package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.fragments.base.BaseFragment

class HistoryFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val history = view.findViewById(R.id.history) as TextView
        history.text = arguments!!.getString(KEY_HISTORY)
    }

    companion object {
        val KEY_HISTORY = "history"
    }
}
