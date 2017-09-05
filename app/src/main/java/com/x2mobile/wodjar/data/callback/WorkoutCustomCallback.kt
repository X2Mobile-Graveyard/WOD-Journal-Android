package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.WorkoutCustomRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutCustomRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class WorkoutCustomCallback : BaseCallback<WorkoutCustom>() {

    override fun onSuccess(call: Call<WorkoutCustom>?, response: Response<WorkoutCustom>) = EventBus.getDefault().post(WorkoutCustomRequestEvent(call, response))

    override fun onFailure(call: Call<WorkoutCustom>?, throwable: Throwable?) = EventBus.getDefault().post(WorkoutCustomRequestFailureEvent(call, throwable))

}