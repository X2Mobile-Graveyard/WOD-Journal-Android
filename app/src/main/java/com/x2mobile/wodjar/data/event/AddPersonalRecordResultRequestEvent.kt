package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import retrofit2.Call
import retrofit2.Response

class AddPersonalRecordResultRequestEvent(call: Call<PersonalRecordResult>?, response: Response<PersonalRecordResult>) : RequestResponseEvent<PersonalRecordResult>(call, response)