package com.x2mobile.wodjar.data.model.adapter

import com.google.gson.stream.JsonReader
import java.io.IOException

class IntResultAdapter : ResultAdapter() {

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Int? {
        return jsonReader.nextInt()
    }

}
