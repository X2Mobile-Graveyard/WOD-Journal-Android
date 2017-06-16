package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.AddWorkoutRequestEvent
import com.x2mobile.wodjar.data.event.AddWorkoutRequestFailureEvent
import com.x2mobile.wodjar.data.model.Workout
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class AddWorkoutCallback : BaseCallback<Workout>() {

    override fun onFailure(call: Call<Workout>?, throwable: Throwable?) {
        EventBus.getDefault().post(AddWorkoutRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<Workout>?, response: Response<Workout>) {
        EventBus.getDefault().post(AddWorkoutRequestEvent(call, response))
    }

}