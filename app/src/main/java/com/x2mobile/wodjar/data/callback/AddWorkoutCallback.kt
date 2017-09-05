package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.AddWorkoutRequestEvent
import com.x2mobile.wodjar.data.event.AddWorkoutRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class AddWorkoutCallback : BaseCallback<WorkoutCustom>() {

    override fun onFailure(call: Call<WorkoutCustom>?, throwable: Throwable?) = EventBus.getDefault().post(AddWorkoutRequestFailureEvent(call, throwable))

    override fun onSuccess(call: Call<WorkoutCustom>?, response: Response<WorkoutCustom>) = EventBus.getDefault().post(AddWorkoutRequestEvent(call, response))

}