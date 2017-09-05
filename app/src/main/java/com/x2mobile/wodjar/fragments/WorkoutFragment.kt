package com.x2mobile.wodjar.fragments

import android.os.Bundle
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.fragments.base.WorkoutBaseFragment
import com.x2mobile.wodjar.ui.callback.WorkoutResultListener
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass

class WorkoutFragment : WorkoutBaseFragment<Workout>(), WorkoutResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Preference.isLoggedIn(context)) {
            Service.getWorkout(workout.id, workout.type)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = workout.name!!
    }

    override fun getWorkoutResultTitle(): String = workout.name!!

    override fun getRequestEventType(): KClass<out RequestResponseEvent<MutableList<Workout>>> = when (workout.type) {
        WorkoutType.GIRLS -> WorkoutGirlsRequestEvent::class
        WorkoutType.HEROES -> WorkoutHeroesRequestEvent::class
        WorkoutType.CHALLENGES -> WorkoutChallengesRequestEvent::class
        WorkoutType.OPENS -> WorkoutOpensRequestEvent::class
        else -> throw UnsupportedOperationException()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResponse(requestResponseEvent: WorkoutRequestEvent) = handleWorkoutResponse(requestResponseEvent)

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutFailure(requestFailureEvent: WorkoutRequestFailureEvent) = handleRequestFailure(requestFailureEvent.throwable)
}
