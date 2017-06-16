package com.x2mobile.wodjar.data.event.base

import retrofit2.Call
import retrofit2.Response

abstract class RequestResponseEvent<T>(val call: Call<T>?, val response: Response<T>)