package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.AddWorkoutResultRequestEvent
import com.x2mobile.wodjar.data.event.AddWorkoutResultRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutResult
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWorkoutResultCallback : Callback<WorkoutResult> {

    override fun onFailure(call: Call<WorkoutResult>?, throwable: Throwable?) {
        EventBus.getDefault().post(AddWorkoutResultRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<WorkoutResult>?, response: Response<WorkoutResult>?) {
        EventBus.getDefault().post(AddWorkoutResultRequestEvent(call, response))
    }

}