package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.LoginResponse
import retrofit2.Call
import retrofit2.Response

class LoginRequestEvent(call: Call<LoginResponse>?, response: Response<LoginResponse>) : RequestResponseEvent<LoginResponse>(call, response)