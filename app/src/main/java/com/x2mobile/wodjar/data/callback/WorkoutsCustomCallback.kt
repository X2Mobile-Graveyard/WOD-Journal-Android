package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.WorkoutsCustomRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutsCustomRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class WorkoutsCustomCallback : BaseCallback<MutableList<WorkoutCustom>>() {

    override fun onSuccess(call: Call<MutableList<WorkoutCustom>>?, response: Response<MutableList<WorkoutCustom>>) {
        EventBus.getDefault().postSticky(WorkoutsCustomRequestEvent(call, response))
    }

    override fun onFailure(call: Call<MutableList<WorkoutCustom>>?, throwable: Throwable?) {
        EventBus.getDefault().post(WorkoutsCustomRequestFailureEvent(call, throwable))
    }

}