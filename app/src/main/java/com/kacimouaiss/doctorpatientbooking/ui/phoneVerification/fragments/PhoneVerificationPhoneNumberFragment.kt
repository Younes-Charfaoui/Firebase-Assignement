package com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kacimouaiss.doctorpatientbooking.R
import com.kacimouaiss.doctorpatientbooking.ui.utils.Utils
import com.kacimouaiss.doctorpatientbooking.ui.utils.toastError
import com.na9ili.na9ilipro.ui.phoneVerification.IPhoneVerificationListener
import kotlinx.android.synthetic.main.fragment_phone_verification_info.view.*


class PhoneVerificationPhoneNumberFragment : Fragment() {

    private lateinit var listener: IPhoneVerificationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as IPhoneVerificationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_verification_info, container, false)

        view.phoneEnterNextButton.setOnClickListener {
            if (!Utils.isNetworkConnected(requireContext())) {
                requireContext().toastError("")
                return@setOnClickListener
            }
            val phoneNumber = view.phoneNumberEditText.text.toString().trim()
            if (!isValidFullNumber(phoneNumber)) {
                view.phoneNumberEditTextLayout.error = "getString(R.string.phone_verification_error_number_phone)"
                return@setOnClickListener
            } else {
                view.phoneNumberEditTextLayout.error = null
            }

            view.progressBarPhoneVerificationInfo.visibility = View.VISIBLE
            view.phoneEnterNextButton.visibility = View.GONE

            Utils.hideKeyboardFrom(requireContext(), view.phoneEnterLayout)
            listener.onPhoneNumberStageCompleted(/*"+1$phoneNumber"*/"+213${phoneNumber.substring(1)}")
        }

        return view
    }

    private fun isValidFullNumber(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty() || phoneNumber.length != 10 || phoneNumber[0] != '0')
            return false
        return true
    }
}