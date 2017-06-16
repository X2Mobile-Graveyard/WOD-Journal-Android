package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class WorkoutFavoriteListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    override fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body()!!.workouts.filter { it.favorite }
            adapter.setItems(workouts.toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

}