package edu.nwtc.myphoneauth.ui.phoneVerification.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.nwtc.myphoneauth.R
import edu.nwtc.myphoneauth.utils.Utils
import edu.nwtc.myphoneauth.utils.toastError
import edu.nwtc.myphoneauth.utils.toastInfo
import edu.nwtc.myphoneauth.ui.phoneVerification.IPhoneVerificationListener
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.*
import kotlinx.android.synthetic.main.fragment_phone_verification_confirm.view.*


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
        val resendTextTimer = "Resend SMS in:"
        val resendSMSString = "Resend SMS"

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
            phone ?: "Your Phone Number"

        view.codePinView.requestFocus()

        view.phoneCodeButton.setOnClickListener {
            if (!Utils.isNetworkConnected(requireContext())) {
                requireContext().toastError("No internet connection, try again later.")
                return@setOnClickListener
            }
            val code = view.codePinView.text?.toString()
            if (code == null || code.isEmpty() || code.length != 6) {
                requireContext().toastInfo("Please enter the full code")
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