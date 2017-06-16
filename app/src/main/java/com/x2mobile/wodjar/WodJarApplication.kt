package com.x2mobile.wodjar

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import org.greenrobot.eventbus.EventBus
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

class WodJarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())

        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {

        lateinit var INSTANCE: Context
    }
}
