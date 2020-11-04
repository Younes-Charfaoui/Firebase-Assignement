package edu.nwtc.myphoneauth.ui.phoneVerification

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
import edu.nwtc.myphoneauth.ui.main.MainActivity
import edu.nwtc.myphoneauth.ui.phoneVerification.fragments.PhoneVerificationDoneFragment
import edu.nwtc.myphoneauth.ui.phoneVerification.fragments.PhoneVerificationPhoneNumberFragment
import edu.nwtc.myphoneauth.utils.fragmentTransition
import edu.nwtc.myphoneauth.utils.toastError
import edu.nwtc.myphoneauth.ui.phoneVerification.fragments.PhoneVerificationCodeVerificationFragment
import com.na9ili.na9ilipro.ui.phoneVerification.PhoneCallbacks
import edu.nwtc.myphoneauth.R
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class PhoneVerificationActivity : AppCompatActivity(), PhoneCallbacks.PhoneCallbacksListener,
    IPhoneVerificationListener {

    private lateinit var firebaseAuthInstance: FirebaseAuth
    private lateinit var mPhoneNumber: String
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneCallbacks: PhoneCallbacks
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        setupDialog()
        phoneCallbacks = PhoneCallbacks(this)

        supportActionBar?.title = "Login"
        fragmentTransition(
            R.id.phoneVerificationContainer,
            PhoneVerificationPhoneNumberFragment()
        )

        firebaseAuthInstance = FirebaseAuth.getInstance()
        firebaseAuthInstance.setLanguageCode(Locale.getDefault().language)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
        if (credential != null) {
            signInWithPhoneAuthCredential(credential)
        } else {
            toastError("Something went wrong")
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
                PhoneVerificationDoneFragment()
            )
        }.addOnFailureListener {
            if (it is FirebaseAuthInvalidCredentialsException) {
                toastError("phone_verification_code_incorrect")
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.phoneVerificationContainer)
                            as PhoneVerificationCodeVerificationFragment
                fragment.showValidateButton()
            } else {
                toastError("general_something_wrong")
                finish()
            }
        }

    }

    override fun onSendAgainTaped() {
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                mPhoneNumber,
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
            toastError("Something wrong")
            finish()
        }
    }

    override fun onPhoneNumberStageCompleted(phoneNumber: String) {
        Timber.d("onPhoneNumberStageCompleted : $phoneNumber")
        this.mPhoneNumber = phoneNumber
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
        dialog.setMessage("Connecting please wait.")
    }
}