package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.data.event.WorkoutChallengesRequestEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WorkoutChallengesListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutChallengesRequestEvent) {
        handleWorkoutsResponse(requestResponseEvent)
    }

}