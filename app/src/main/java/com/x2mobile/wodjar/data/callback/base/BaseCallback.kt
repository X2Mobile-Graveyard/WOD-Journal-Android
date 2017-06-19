package com.x2mobile.wodjar.data.callback.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.x2mobile.wodjar.business.network.exception.ServerException
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import com.x2mobile.wodjar.data.model.ErrorBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T> : Callback<T> {

    val HTTP_CODE_UNAUTHORIZED = 401

    final override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response == null || !response.isSuccessful) {
            onFailure(call, handleFailureCode(response?.raw()?.code(), response?.errorBody()))
        } else {
            onSuccess(call, response)
        }
    }

    protected abstract fun onSuccess(call: Call<T>?, response: Response<T>)

    private fun handleFailureCode(code: Int?, errorBody: ResponseBody?): RuntimeException? {
        if (code == null) {
            return null
        } else if (code == HTTP_CODE_UNAUTHORIZED) {
            return UnauthorizedException(code, handleErrorMessage(errorBody))
        } else {
            return ServerException(code, handleErrorMessage(errorBody))
        }
    }

    private fun handleErrorMessage(errorBody: ResponseBody?): List<String>? {
        if (errorBody != null) {
            return Gson().fromJson<ErrorBody>(errorBody.string(), object : TypeToken<ErrorBody>() {}.type).errors
        }
        return null
    }
}
