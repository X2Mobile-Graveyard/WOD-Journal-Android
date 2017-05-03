package com.x2mobile.wodjar.data.model.adapter

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class UriAdapter : TypeAdapter<Uri>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Uri) {
        out.value(value.toString())
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Uri? {
        return Uri.parse(jsonReader.nextString())
    }

}
