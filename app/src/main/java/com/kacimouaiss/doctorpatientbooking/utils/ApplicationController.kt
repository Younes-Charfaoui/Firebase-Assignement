package com.kacimouaiss.doctorpatientbooking.utils

import android.app.Application
import com.kacimouaiss.doctorpatientbooking.BuildConfig
import com.yariksoffice.lingver.Lingver
import timber.log.Timber

@Suppress("unused")
class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}