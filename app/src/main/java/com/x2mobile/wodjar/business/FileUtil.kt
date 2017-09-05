package com.x2mobile.wodjar.business

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Pair
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

object FileUtil {

    private val CONTENT_SCHEME = "content"
    private val FILE_SCHEME = "file"

    fun prepareForUpload(context: Context, uri: Uri): Pair<InputStream, Long>? {
        if (CONTENT_SCHEME.equals(uri.scheme, ignoreCase = true)) {
            val contentResolver = context.contentResolver
            val projection = arrayOf(MediaStore.Images.Media.SIZE)
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(uri, projection, null, null, null)
                if (cursor == null) {
                    return null
                }
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                if (cursor.moveToFirst()) {
                    return Pair.create(contentResolver.openInputStream(uri), cursor.getLong(sizeIndex))
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        } else if (FILE_SCHEME.equals(uri.scheme, ignoreCase = true)) {
            val file = File(uri.path)
            try {
                return Pair.create<InputStream, Long>(FileInputStream(file), file.length())
            } catch (exception: FileNotFoundException) {
                exception.printStackTrace()
            }

        }

        return null
    }

}