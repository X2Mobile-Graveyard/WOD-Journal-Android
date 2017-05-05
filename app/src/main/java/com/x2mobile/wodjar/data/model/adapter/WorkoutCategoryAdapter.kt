package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.x2mobile.wodjar.data.model.WorkoutCategory
import java.io.IOException

class WorkoutCategoryAdapter : TypeAdapter<WorkoutCategory>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: WorkoutCategory) {
        out.value(value.ordinal)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): WorkoutCategory? {
        return WorkoutCategory.values()[jsonReader.nextString()!!.toInt()]
    }

}
