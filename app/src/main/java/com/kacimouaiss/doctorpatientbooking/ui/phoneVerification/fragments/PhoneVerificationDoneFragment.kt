package com.kacimouaiss.doctorpatientbooking.ui.phoneVerification.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kacimouaiss.doctorpatientbooking.R
import com.na9ili.na9ilipro.ui.phoneVerification.IPhoneVerificationListener
import kotlinx.android.synthetic.main.fragment_phone_verification_done.view.*


class PhoneVerificationDoneFragment : Fragment() {

    companion object {
        private const val KEY_TYPE = "keyType"
        const val KEY_SIGN_UP = 156
        const val KEY_LOGIN = 114
        fun newInstance(key: Int) = PhoneVerificationDoneFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_TYPE, key)
            }
        }
    }

    private lateinit var listener: IPhoneVerificationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as IPhoneVerificationListener
    }

    private val callbacks = Runnable { listener.onCompleteVerification() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_verification_done, container, false)

        val type = arguments?.getInt(KEY_TYPE, -1)

        if (type == KEY_SIGN_UP)
            view.textDescriptionAnimationCheckout.text = ""

        view.phoneDoneButton.setOnClickListener {
            listener.onCompleteVerification()
            Handler(Looper.getMainLooper()).removeCallbacks(callbacks)
        }

        Handler(Looper.getMainLooper()).postDelayed(callbacks, 4000)
        return view
    }
}