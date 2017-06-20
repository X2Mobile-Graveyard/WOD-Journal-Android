package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.BuildConfig
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.fragments.base.BaseFragment

class OptionsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildNumber = view.findViewById(R.id.build_number) as TextView
        buildNumber.text = getString(R.string.version_info, BuildConfig.VERSION_NAME, BuildConfig.BUILD_NUMBER)
    }
}
