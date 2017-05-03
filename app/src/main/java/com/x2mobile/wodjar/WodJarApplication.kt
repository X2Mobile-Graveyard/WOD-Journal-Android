package com.x2mobile.wodjar

import android.app.Application
import android.content.Context

class WodJarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {

        var INSTANCE: Context? = null
    }
}
