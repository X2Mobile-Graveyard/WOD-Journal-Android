package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.AddPersonalRecordRequestEvent
import com.x2mobile.wodjar.data.event.AddPersonalRecordRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPersonalRecordCallback : Callback<PersonalRecord> {

    override fun onFailure(call: Call<PersonalRecord>?, throwable: Throwable?) {
        EventBus.getDefault().post(AddPersonalRecordRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<PersonalRecord>?, response: Response<PersonalRecord>?) {
        EventBus.getDefault().post(AddPersonalRecordRequestEvent(call, response))
    }

}