package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.PersonalRecordTypesResponse
import retrofit2.Call
import retrofit2.Response

class PersonalRecordTypesRequestEvent(call: Call<PersonalRecordTypesResponse>?, response: Response<PersonalRecordTypesResponse>?) : RequestResponseEvent<PersonalRecordTypesResponse>(call, response)