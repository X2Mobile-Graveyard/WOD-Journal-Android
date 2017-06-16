package com.x2mobile.wodjar.util

import android.net.Uri

const val HTTP_SCHEME = "http"
const val HTTPS_SCHEME = "https"

fun Uri.isUrl(): Boolean {
    return this.scheme == HTTP_SCHEME || this.scheme == HTTPS_SCHEME
}