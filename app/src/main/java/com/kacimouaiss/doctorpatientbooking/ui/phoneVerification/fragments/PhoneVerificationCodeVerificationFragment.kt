package com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kacimouaiss.doctorpatientbooking.R
import com.kacimouaiss.doctorpatientbooking.utils.Utils
import com.kacimouaiss.doctorpatientbooking.utils.toastError
import com.kacimouaiss.doctorpatientbooking.utils.toastInfo
import com.na9ili.na9ilipro.ui.phoneVerification.IPhoneVerificationListener
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.*
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.view.*
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.view.phoneCodeButton
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.view.validateProgressBar


class PhoneVerificationCodeVerificationFragment : Fragment() {

    private lateinit var listener: IPhoneVerificationListener

    companion object {
        private const val KEY_PHONE = "keyPhone"

        fun newInstance(phoneNumber: String) = PhoneVerificationCodeVerificationFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_PHONE, phoneNumber)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as IPhoneVerificationListener
    }

    private lateinit var countDownTimer: CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_verification_confirm, container, false)
        val phone = arguments?.getString(KEY_PHONE)
        val resendTextTimer = "getString(R.string.phone_verification_resend_sms_in)"
        val resendSMSString = "getString(R.string.phone_verification_resend_sms_string)"

        countDownTimer = object : CountDownTimer(60_000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                view.sendAgainTextView.text = resendTextTimer + millisUntilFinished / 1000
            }

            override fun onFinish() {
                view.sendAgainTextView.text = resendSMSString
                view.sendAgainTextView.isEnabled = true
                view.sendAgainTextView.isClickable = true
            }
        }

        view.sendAgainTextView.isEnabled = false
        view.sendAgainTextView.isClickable = false

        view.phoneNumberTextView.text =
            phone ?: "getString(R.string.phone_verification_your_phone_number)"

        view.codePinView.requestFocus()

        view.phoneCodeButton.setOnClickListener {
            if (!Utils.isNetworkConnected(requireContext())) {
                requireContext().toastError("R.string.general_no_internet_message")
                return@setOnClickListener
            }
            val code = view.codePinView.text?.toString()
            if (code == null || code.isEmpty() || code.length != 6) {
                requireContext().toastInfo("R.string.phone_verification_error_enter_valid_code")
                return@setOnClickListener
            } else {
                view.validateProgressBar.visibility = View.VISIBLE
                view.phoneCodeButton.visibility = View.GONE
                listener.onCodeEntered(code)
                Utils.hideKeyboardFrom(requireContext(), view.phoneCodeLayout)
            }
        }

        view.sendAgainTextView.setOnClickListener {
            listener.onSendAgainTaped()
            view.sendAgainTextView.isEnabled = false
            view.sendAgainTextView.isClickable = false
            countDownTimer.start()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        countDownTimer.start()
    }

    fun showValidateButton() {
        validateProgressBar.visibility = View.GONE
        phoneCodeButton.visibility = View.VISIBLE
    }
}