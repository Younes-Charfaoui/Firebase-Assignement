package com.na9ili.na9ilipro.ui.phoneVerification

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneCallbacks(private val listener : PhoneCallbacksListener) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    interface PhoneCallbacksListener {
        fun onVerificationCompleted(credential: PhoneAuthCredential?)
        fun onVerificationFailed(exception: FirebaseException?)
        fun onCodeSent(
            verificationId: String?,
            token: PhoneAuthProvider.ForceResendingToken?
        )
    }

    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        listener.onVerificationCompleted(phoneAuthCredential)
    }

    override fun onVerificationFailed(exception: FirebaseException) {
        listener.onVerificationFailed(exception)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        listener.onCodeSent(verificationId,token)
    }
}