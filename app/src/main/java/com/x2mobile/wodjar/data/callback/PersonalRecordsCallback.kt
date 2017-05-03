package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.PersonalRecordsRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordsRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordsResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalRecordsCallback : Callback<PersonalRecordsResponse> {

    override fun onFailure(call: Call<PersonalRecordsResponse>?, throwable: Throwable?) {
        EventBus.getDefault().post(PersonalRecordsRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<PersonalRecordsResponse>?, response: Response<PersonalRecordsResponse>?) {
        EventBus.getDefault().post(PersonalRecordsRequestEvent(call, response))
    }

}