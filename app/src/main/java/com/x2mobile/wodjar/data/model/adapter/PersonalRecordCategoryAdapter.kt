package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.x2mobile.wodjar.data.model.PersonalRecordCategory
import java.io.IOException

class PersonalRecordCategoryAdapter : TypeAdapter<PersonalRecordCategory>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: PersonalRecordCategory) {
        out.value(value.ordinal)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): PersonalRecordCategory? {
        return PersonalRecordCategory.values()[jsonReader.nextString()!!.toInt()]
    }

}
