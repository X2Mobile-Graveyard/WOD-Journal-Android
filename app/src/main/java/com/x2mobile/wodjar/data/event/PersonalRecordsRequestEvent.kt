package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.PersonalRecordsResponse
import retrofit2.Call
import retrofit2.Response

class PersonalRecordsRequestEvent(call: Call<PersonalRecordsResponse>?, response: Response<PersonalRecordsResponse>?) : RequestResponseEvent<PersonalRecordsResponse>(call, response)