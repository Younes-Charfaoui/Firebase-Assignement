package com.na9ili.na9ilipro.ui.phoneVerification

interface IPhoneVerificationListener {

    fun onPhoneNumberStageCompleted(phoneNumber: String)
    fun onCodeEntered(code: String)
    fun onSendAgainTaped()
    fun onCompleteVerification()
}