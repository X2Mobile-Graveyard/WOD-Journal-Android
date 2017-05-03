package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.LoginRequestEvent
import com.x2mobile.wodjar.data.event.LoginRequestFailureEvent
import com.x2mobile.wodjar.data.model.LoginResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginCallback : Callback<LoginResponse> {

    override fun onFailure(call: Call<LoginResponse>?, throwable: Throwable?) {
        EventBus.getDefault().post(LoginRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
        EventBus.getDefault().post(LoginRequestEvent(call, response))
    }

}