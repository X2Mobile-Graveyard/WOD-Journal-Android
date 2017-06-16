package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutCustomActivity
import com.x2mobile.wodjar.activity.WorkoutEditActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.Workout
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick

class WorkoutCustomListFragment : WorkoutListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val add = view.findViewById(R.id.add)
        add.onClick {
            if (Preference.isLoggedIn(context)) {
                startActivity(context.intentFor<WorkoutEditActivity>())
            } else {
                showLoginAlert()
            }
        }
    }

    override fun onWorkoutClicked(workout: Workout) {
        startActivity(context.intentFor<WorkoutCustomActivity>(NavigationConstants.KEY_WORKOUT to workout))
    }
}