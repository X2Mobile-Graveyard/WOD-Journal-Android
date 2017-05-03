package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.PersonalRecordTypesRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordTypesRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordTypesResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalRecordTypesCallback : Callback<PersonalRecordTypesResponse> {

    override fun onFailure(call: Call<PersonalRecordTypesResponse>?, throwable: Throwable?) {
        EventBus.getDefault().post(PersonalRecordTypesRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<PersonalRecordTypesResponse>?, response: Response<PersonalRecordTypesResponse>?) {
        EventBus.getDefault().post(PersonalRecordTypesRequestEvent(call, response))
    }

}