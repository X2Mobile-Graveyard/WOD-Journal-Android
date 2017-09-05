package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.DeleteWorkoutRequestEvent
import com.x2mobile.wodjar.data.event.DeleteWorkoutRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class DeleteWorkoutCallback : BaseCallback<Void>() {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) = EventBus.getDefault().post(DeleteWorkoutRequestFailureEvent(call, throwable))

    override fun onSuccess(call: Call<Void>?, response: Response<Void>) = EventBus.getDefault().post(DeleteWorkoutRequestEvent(call, response))

}