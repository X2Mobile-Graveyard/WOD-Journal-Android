package com.x2mobile.wodjar.data.event.base

import retrofit2.Call

abstract class RequestFailureEvent<T>(val call: Call<T>?, val throwable: Throwable?)