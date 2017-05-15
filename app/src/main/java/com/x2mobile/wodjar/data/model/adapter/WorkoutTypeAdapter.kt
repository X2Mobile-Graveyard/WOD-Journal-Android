package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.x2mobile.wodjar.data.model.WorkoutType
import java.io.IOException

class WorkoutTypeAdapter : TypeAdapter<WorkoutType>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: WorkoutType) {
        out.value(value.ordinal)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): WorkoutType? {
        return WorkoutType.values()[jsonReader.nextString()!!.toInt()]
    }

}
