package com.x2mobile.wodjar.data.callback.base

import com.x2mobile.wodjar.business.network.exception.ServerException
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T> : Callback<T> {

    val HTTP_CODE_UNAUTHORIZED = 401

    final override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response == null || !response.isSuccessful) {
            onFailure(call, handleFailureCode(response?.raw()?.code()))
        } else {
            onSuccess(call, response)
        }
    }

    protected abstract fun onSuccess(call: Call<T>?, response: Response<T>)

    private fun handleFailureCode(code: Int?): RuntimeException? {
        if (code == null) {
            return null
        } else if (code == HTTP_CODE_UNAUTHORIZED) {
            return UnauthorizedException()
        } else {
            return ServerException(code)
        }
    }
}
