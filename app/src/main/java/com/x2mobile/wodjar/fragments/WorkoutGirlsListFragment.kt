package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.data.event.WorkoutGirlsRequestEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WorkoutGirlsListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutGirlsRequestEvent) {
        handleWorkoutsResponse(requestResponseEvent)
    }

}