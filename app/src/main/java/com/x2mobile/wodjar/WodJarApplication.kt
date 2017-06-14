package com.x2mobile.wodjar

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import org.greenrobot.eventbus.EventBus

class WodJarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {

        var INSTANCE: Context? = null
    }
}
