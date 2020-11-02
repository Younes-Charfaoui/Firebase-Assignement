package com.kacimouaiss.doctorpatientbooking.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kacimouaiss.doctorpatientbooking.R
import com.kacimouaiss.doctorpatientbooking.firebase.RemoteConfigUtils
import com.kacimouaiss.doctorpatientbooking.ui.main.MainActivity
import com.na9ili.na9ilipro.ui.phoneVerification.PhoneVerificationActivity
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var splashTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Glide.with(this).load(R.drawable.ic_launcher_background).into(nakiliLogo)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        splashTime = RemoteConfigUtils.getSplashTime()
        Timber.d("onCreate : $splashTime")

        splashTime()
    }

    private fun routeUser(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            startActivity(Intent(this, PhoneVerificationActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun splashTime() {
        Handler().postDelayed({
            routeUser(firebaseAuth.currentUser)
        }, splashTime)
    }
}
