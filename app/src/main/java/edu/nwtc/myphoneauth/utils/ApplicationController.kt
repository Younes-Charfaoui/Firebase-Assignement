package edu.nwtc.myphoneauth.utils

import android.app.Application
import edu.nwtc.myphoneauth.BuildConfig
import com.yariksoffice.lingver.Lingver
import timber.log.Timber

class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}