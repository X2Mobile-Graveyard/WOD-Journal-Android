package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.x2mobile.wodjar.data.model.ResultType
import java.io.IOException

class ResultTypeAdapter : TypeAdapter<ResultType>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: ResultType) {
        out.value(value.ordinal)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): ResultType? = ResultType.values()[jsonReader.nextInt()]

}
