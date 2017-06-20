package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.activity.WorkoutActivity
import com.x2mobile.wodjar.activity.WorkoutCustomActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import org.jetbrains.anko.intentFor

class WorkoutFavoriteListFragment : WorkoutListFragment() {

    override fun onWorkoutClicked(workout: Workout) {
        if (workout.type == WorkoutType.CUSTOM) {
            startActivity(context.intentFor<WorkoutCustomActivity>(NavigationConstants.KEY_WORKOUT to workout))
        } else {
            startActivity(context.intentFor<WorkoutActivity>(NavigationConstants.KEY_WORKOUT to workout))
        }
    }

    override fun filterWorkouts(workouts: List<Workout>): List<Workout> {
        return workouts.filter { it.favorite }.sortedBy(Workout::name)
    }

}