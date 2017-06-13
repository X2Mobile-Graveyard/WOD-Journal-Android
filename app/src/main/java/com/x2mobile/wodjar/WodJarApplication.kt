package com.x2mobile.wodjar

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class WodJarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {

        var INSTANCE: Context? = null
    }
}
