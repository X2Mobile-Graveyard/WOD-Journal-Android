package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.fragments.base.WorkoutBaseListFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutsAdapter
import org.jetbrains.anko.intentFor

open class WorkoutListFragment : WorkoutBaseListFragment<Workout>() {

    override val adapter: WorkoutsAdapter by lazy { WorkoutsAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.search_menu)) as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
    }

    override fun onWorkoutClicked(workout: Workout) {
        startActivity(context.intentFor<WorkoutActivity>(NavigationConstants.KEY_WORKOUT to workout))
    }

    override fun sortWorkouts(workouts: List<Workout>): List<Workout> {
        return workouts.sortedBy(Workout::name)
    }

}
