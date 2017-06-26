package com.x2mobile.wodjar

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import org.greenrobot.eventbus.EventBus

class WodJarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {

        lateinit var INSTANCE: Context
    }
}
