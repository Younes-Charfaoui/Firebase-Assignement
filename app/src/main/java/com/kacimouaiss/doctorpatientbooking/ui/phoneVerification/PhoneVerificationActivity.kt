package com.kacimouaiss.doctorpatientbooking.ui.phoneVerification

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.kacimouaiss.doctorpatientbooking.R
import com.kacimouaiss.doctorpatientbooking.ui.main.MainActivity
import com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments.PhoneVerificationDoneFragment
import com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments.PhoneVerificationDoneFragment.Companion.KEY_SIGN_UP
import com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments.PhoneVerificationPhoneNumberFragment
import com.kacimouaiss.doctorpatientbooking.utils.fragmentTransition
import com.kacimouaiss.doctorpatientbooking.utils.toastError
import com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments.PhoneVerificationCodeVerificationFragment
import com.na9ili.na9ilipro.ui.phoneVerification.IPhoneVerificationListener
import com.na9ili.na9ilipro.ui.phoneVerification.PhoneCallbacks
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class PhoneVerificationActivity : AppCompatActivity(), PhoneCallbacks.PhoneCallbacksListener,
    IPhoneVerificationListener {

    companion object {
        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
        const val KEY_RESULT_VERIFICATION = "KEY_RESULT_VERIFICATION"
        const val KEY_ERROR_TYPE = "KEY_ERROR_TYPE"
        const val KEY_ERROR_ALREADY_EXIST = 1154
    }

    private lateinit var firebaseAuthInstance: FirebaseAuth
    private lateinit var maidPhoneNumber: String
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneCallbacks: PhoneCallbacks
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        setupDialog()
        phoneCallbacks = PhoneCallbacks(this)

        supportActionBar?.title = "Connect"
        fragmentTransition(
            R.id.phoneVerificationContainer,
            PhoneVerificationPhoneNumberFragment()
        )


        firebaseAuthInstance = FirebaseAuth.getInstance()
        firebaseAuthInstance.setLanguageCode(Locale.getDefault().language)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
        if (credential != null) {
            Timber.d("Crediential was n't null, Im here")
            signInWithPhoneAuthCredential(credential)
        } else {
            Timber.d("Crediential was nukk Im here Too")
            toastError("R.string.general_something_wrong")
            finish()
        }
    }

    override fun onVerificationFailed(exception: FirebaseException?) {
        Timber.d(exception, "Error Verification faild")
        toastError("Error Verification faild")
        finish()
    }

    override fun onCodeSent(
        verificationId: String?,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        storedVerificationId = verificationId!!
        resendToken = token!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuthInstance.signInWithCredential(credential).addOnSuccessListener {
            fragmentTransition(
                R.id.phoneVerificationContainer,
                PhoneVerificationDoneFragment.newInstance(KEY_SIGN_UP)
            )
        }.addOnFailureListener {
            if (it is FirebaseAuthInvalidCredentialsException) {
                toastError("getString(R.string.phone_verification_code_incorrect)")
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.phoneVerificationContainer)
                            as PhoneVerificationCodeVerificationFragment
                fragment.showValidateButton()
            } else {
                toastError("R.string.general_something_wrong")
                finish()
            }
        }

    }

    override fun onSendAgainTaped() {
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                maidPhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                phoneCallbacks,
                resendToken
            )
    }

    override fun onCompleteVerification() {
        Timber.d("InI")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCodeEntered(code: String) {
        if (storedVerificationId != null) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        } else {
            toastError("getString(R.string.general_something_wrong)")
            finish()
        }
    }

    override fun onPhoneNumberStageCompleted(phoneNumber: String) {
        //val formattedPhone = /*"+1$phoneNumber"*/"+213${phoneNumber.substring(1)}"
        Timber.d("onPhoneNumberStageCompleted : $phoneNumber")
        this.maidPhoneNumber = phoneNumber
        sendSmsAndConfirm(phoneNumber)
    }

    private fun sendSmsAndConfirm(phoneNumber: String) {
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, phoneCallbacks)
        fragmentTransition(
            R.id.phoneVerificationContainer,
            PhoneVerificationCodeVerificationFragment.newInstance(phoneNumber)
        )
    }

    private fun setupDialog() {
        dialog = ProgressDialog(this)
        dialog.setMessage("getString(R.string.general_connecting_please_wait_string)")
    }
}