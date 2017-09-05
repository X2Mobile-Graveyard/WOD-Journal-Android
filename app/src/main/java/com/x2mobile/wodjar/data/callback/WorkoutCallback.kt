package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.WorkoutRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutRequestFailureEvent
import com.x2mobile.wodjar.data.model.Workout
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class WorkoutCallback : BaseCallback<Workout>() {

    override fun onSuccess(call: Call<Workout>?, response: Response<Workout>) = EventBus.getDefault().post(WorkoutRequestEvent(call, response))

    override fun onFailure(call: Call<Workout>?, throwable: Throwable?) = EventBus.getDefault().post(WorkoutRequestFailureEvent(call, throwable))

}