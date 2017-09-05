package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.data.event.WorkoutHeroesRequestEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WorkoutHeroesListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutHeroesRequestEvent) = handleWorkoutsResponse(requestResponseEvent)

}