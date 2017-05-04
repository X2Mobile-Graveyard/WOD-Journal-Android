package com.x2mobile.wodjar.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.x2mobile.wodjar.business.callback.NavigationDrawerCallback
import com.x2mobile.wodjar.fragments.NavigationDrawerFragment
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.callback.ToolbarDelegate
import com.x2mobile.wodjar.databinding.MainScreenBinding
import com.x2mobile.wodjar.fragments.PersonalRecordTypeFragment
import com.x2mobile.wodjar.fragments.WorkoutTypeFragment


class MainMenuActivity : AppCompatActivity(), NavigationDrawerCallback, ToolbarDelegate {

    private var mainScreenBinding: MainScreenBinding? = null

    override var title: String
        get() = supportActionBar!!.title.toString()
        set(value) {
            supportActionBar!!.title = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainScreenBinding = DataBindingUtil.setContentView<MainScreenBinding>(this, R.layout.main_screen)

        setSupportActionBar(mainScreenBinding!!.toolbar)

        mainScreenBinding!!.toolbar!!.setNavigationOnClickListener { mainScreenBinding!!.drawer!!.openDrawer(GravityCompat.START) }
    }

    override fun onNavigationItemSelected(navigationType: NavigationDrawerFragment.NavigationType) {
        val selectedFragment: Fragment?
        when (navigationType) {
            NavigationDrawerFragment.NavigationType.PR -> selectedFragment = PersonalRecordTypeFragment()
            NavigationDrawerFragment.NavigationType.WOD -> selectedFragment = WorkoutTypeFragment()
            else -> throw RuntimeException("Navigation type: $navigationType not supported")
        }
        // update the main content by replacing fragments
        supportFragmentManager.beginTransaction().replace(R.id.container, selectedFragment).commit()

        mainScreenBinding?.drawer?.closeDrawer(GravityCompat.START)
    }

    override fun showIndeterminateLoading(show: Boolean) {
    }

    override fun enableTitleChange() {
    }

    override fun startTitleChange() {
    }
}


