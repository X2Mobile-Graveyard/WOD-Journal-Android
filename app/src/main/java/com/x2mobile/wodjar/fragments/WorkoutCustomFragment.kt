package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutEditActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.ui.binding.model.WorkoutViewModel
import org.jetbrains.anko.intentFor

class WorkoutCustomFragment : WorkoutFragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.edit_menu).isVisible = workout.type == WorkoutType.CUSTOM
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_menu) {
            startActivityForResult(context.intentFor<WorkoutEditActivity>(NavigationConstants.KEY_WORKOUT to workout), REQUEST_CODE_EDIT_WORKOUT)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EDIT_WORKOUT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val cachedWorkout = data?.getParcelableExtra<Workout>(NavigationConstants.KEY_WORKOUT)!!
                    workout.name = cachedWorkout.name
                    workout.description = cachedWorkout.description
                    workout.imageUri = cachedWorkout.imageUri
                    arguments.putParcelable(NavigationConstants.KEY_WORKOUT, workout)

                    binding.viewModel = WorkoutViewModel(context, workout)

                    toolbarDelegate.title = workout.name!!

                    imageViewer.imageUri = workout.imageUri
                }
                NavigationConstants.RESULT_DELETED -> activity.finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}