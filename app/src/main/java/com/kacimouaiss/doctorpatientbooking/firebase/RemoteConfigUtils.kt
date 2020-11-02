package com.kacimouaiss.doctorpatientbooking.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kacimouaiss.doctorpatientbooking.BuildConfig
import com.kacimouaiss.doctorpatientbooking.R
import com.kacimouaiss.doctorpatientbooking.constants.RemoteConfigConstants
import java.util.concurrent.TimeUnit

object RemoteConfigUtils {

    private val remoteConfigInstance: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
            this.setConfigSettingsAsync(configSettings)
            this.setDefaultsAsync(R.xml.remote_config_defaults)
            this.activate()
            this.fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(12))
        }
    }

    fun getSplashTime() =
        remoteConfigInstance.getDouble(RemoteConfigConstants.SPLASH_TIME_KEY).toLong()

    fun getOpeningAndClosingTimes() =
        remoteConfigInstance.getLong(RemoteConfigConstants.OPENING_TIME).toInt() to
                remoteConfigInstance.getLong(RemoteConfigConstants.CLOSING_TIME).toInt()

    fun getTApplicationOpen() =
        remoteConfigInstance.getBoolean(RemoteConfigConstants.OPEN_APP_KEY)

}