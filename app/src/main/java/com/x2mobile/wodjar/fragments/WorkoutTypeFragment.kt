package com.x2mobile.wodjar.fragments

import android.os.Bundle
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutsRequestFailureEvent
import com.x2mobile.wodjar.fragments.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class WorkoutTypeFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        Service.getWorkouts()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.workouts)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body().workouts
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutsFailure(requestFailureEvent: WorkoutsRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }
}
