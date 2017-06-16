package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.UpdateWorkoutResultRequestEvent
import com.x2mobile.wodjar.data.event.UpdateWorkoutResultRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class UpdateWorkoutResultCallback : BaseCallback<Void>() {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(UpdateWorkoutResultRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<Void>?, response: Response<Void>) {
        EventBus.getDefault().post(UpdateWorkoutResultRequestEvent(call, response))
    }

}