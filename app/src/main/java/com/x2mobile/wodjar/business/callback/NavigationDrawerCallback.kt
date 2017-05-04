package com.x2mobile.wodjar.business.callback

import com.x2mobile.wodjar.fragments.NavigationDrawerFragment

/**
 * Callbacks interface that all activities using this fragment must implement.
 */
interface NavigationDrawerCallback {

    /**
     * Called when an item in the navigation_header drawer is selected.
     */
    fun onNavigationItemSelected(navigationType: NavigationDrawerFragment.NavigationType)
}
