package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.DeleteWorkoutResultRequestEvent
import com.x2mobile.wodjar.data.event.DeleteWorkoutResultRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteWorkoutResultCallback : Callback<Void> {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(DeleteWorkoutResultRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
        EventBus.getDefault().post(DeleteWorkoutResultRequestEvent(call, response))
    }

}