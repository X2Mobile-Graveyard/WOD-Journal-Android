package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.AddWorkoutResultRequestEvent
import com.x2mobile.wodjar.data.event.AddWorkoutResultRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutResult
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class AddWorkoutResultCallback : BaseCallback<WorkoutResult>() {

    override fun onFailure(call: Call<WorkoutResult>?, throwable: Throwable?) = EventBus.getDefault().post(AddWorkoutResultRequestFailureEvent(call, throwable))

    override fun onSuccess(call: Call<WorkoutResult>?, response: Response<WorkoutResult>) = EventBus.getDefault().post(AddWorkoutResultRequestEvent(call, response))

}