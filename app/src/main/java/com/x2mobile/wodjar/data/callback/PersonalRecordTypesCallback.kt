package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.PersonalRecordTypesRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordTypesRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordTypesResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class PersonalRecordTypesCallback : BaseCallback<PersonalRecordTypesResponse>() {

    override fun onFailure(call: Call<PersonalRecordTypesResponse>?, throwable: Throwable?) {
        EventBus.getDefault().post(PersonalRecordTypesRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<PersonalRecordTypesResponse>?, response: Response<PersonalRecordTypesResponse>) {
        EventBus.getDefault().postSticky(PersonalRecordTypesRequestEvent(call, response))
    }

}