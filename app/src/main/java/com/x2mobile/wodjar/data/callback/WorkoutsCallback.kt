package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class WorkoutsCallback(val workoutType: WorkoutType) : BaseCallback<MutableList<Workout>>() {

    override fun onSuccess(call: Call<MutableList<Workout>>?, response: Response<MutableList<Workout>>) {
        EventBus.getDefault().postSticky(when (workoutType) {
            WorkoutType.GIRLS -> WorkoutGirlsRequestEvent(call, response)
            WorkoutType.HEROES -> WorkoutHeroesRequestEvent(call, response)
            WorkoutType.CHALLENGES -> WorkoutChallengesRequestEvent(call, response)
            WorkoutType.OPENS -> WorkoutOpensRequestEvent(call, response)
            WorkoutType.CUSTOM -> WorkoutCustomsRequestEvent(call, response)
        })
    }

    override fun onFailure(call: Call<MutableList<Workout>>?, throwable: Throwable?) {
        EventBus.getDefault().post(WorkoutsRequestFailureEvent(call, throwable))
    }

}