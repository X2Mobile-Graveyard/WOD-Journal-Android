package com.x2mobile.wodjar.activity

import android.os.Bundle
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.base.BaseToolbarActivity
import com.x2mobile.wodjar.fragments.PersonalRecordFragment

class PersonalRecordActivity : BaseToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
        if (savedInstanceState == null) {
            val fragment = PersonalRecordFragment()
            fragment.arguments = intent.extras
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }
}
