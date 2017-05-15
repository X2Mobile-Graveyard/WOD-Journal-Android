package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.x2mobile.wodjar.fragments.base.BaseFragment

class WorkoutResultFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    companion object {
        val KEY_WORKOUT_RESULT = "workout_result"
    }
}