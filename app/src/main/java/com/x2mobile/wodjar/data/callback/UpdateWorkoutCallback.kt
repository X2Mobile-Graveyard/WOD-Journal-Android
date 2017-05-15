package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.UpdateWorkoutRequestEvent
import com.x2mobile.wodjar.data.event.UpdateWorkoutRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateWorkoutCallback : Callback<Void> {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(UpdateWorkoutRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
        EventBus.getDefault().post(UpdateWorkoutRequestEvent(call, response))
    }

}