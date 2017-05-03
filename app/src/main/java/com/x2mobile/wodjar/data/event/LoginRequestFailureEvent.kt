package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.LoginResponse
import retrofit2.Call

class LoginRequestFailureEvent(call: Call<LoginResponse>?, throwable: Throwable?) : RequestFailureEvent<LoginResponse>(call, throwable)