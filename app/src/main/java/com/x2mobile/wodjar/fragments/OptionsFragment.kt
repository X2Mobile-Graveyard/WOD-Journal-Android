package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.callback.ToolbarDelegate

class OptionsFragment : PreferenceFragmentCompat() {

    val toolbarDelegate: ToolbarDelegate by lazy { activity as ToolbarDelegate }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.options)
    }
}
