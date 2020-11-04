package edu.nwtc.myphoneauth.ui.phoneVerification.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.nwtc.myphoneauth.R
import edu.nwtc.myphoneauth.ui.phoneVerification.IPhoneVerificationListener
import edu.nwtc.myphoneauth.utils.Utils
import edu.nwtc.myphoneauth.utils.toastError
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
                view.phoneNumberEditTextLayout.error = "phone_verification_error_number_phone"
                return@setOnClickListener
            } else {
                view.phoneNumberEditTextLayout.error = null
            }

            view.progressBarPhoneVerificationInfo.visibility = View.VISIBLE
            view.phoneEnterNextButton.visibility = View.GONE

            Utils.hideKeyboardFrom(requireContext(), view.phoneEnterLayout)
            listener.onPhoneNumberStageCompleted("+1$phoneNumber")
        }

        return view
    }

    private fun isValidFullNumber(phoneNumber: String): Boolean {
        return true
    }
}