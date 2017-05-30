package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.UpdateUserRequestEvent
import com.x2mobile.wodjar.data.event.UpdateUserRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserCallback : Callback<Void> {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(UpdateUserRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
        EventBus.getDefault().post(UpdateUserRequestEvent(call, response))
    }

}