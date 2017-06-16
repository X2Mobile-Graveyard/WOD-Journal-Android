package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import java.io.IOException

abstract class ResultAdapter : TypeAdapter<Number>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Number) {
        if (value.toInt() == 0) {
            out.nullValue()
        } else {
            out.value(value)
        }
    }

}
