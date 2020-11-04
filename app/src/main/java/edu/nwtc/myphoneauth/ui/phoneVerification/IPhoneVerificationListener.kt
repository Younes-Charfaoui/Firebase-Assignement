package edu.nwtc.myphoneauth.ui.phoneVerification

interface IPhoneVerificationListener {

    fun onPhoneNumberStageCompleted(phoneNumber: String)
    fun onCodeEntered(code: String)
    fun onSendAgainTaped()
    fun onCompleteVerification()
}