package edu.nwtc.myphoneauth.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.nwtc.myphoneauth.R
import edu.nwtc.myphoneauth.ui.main.MainActivity
import edu.nwtc.myphoneauth.ui.phoneVerification.PhoneVerificationActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var splashTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Glide.with(this).load(R.drawable.ic_baseline_person_24).into(nakiliLogo)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

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
