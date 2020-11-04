package edu.nwtc.myphoneauth.ui.phoneVerification.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.nwtc.myphoneauth.R
import edu.nwtc.myphoneauth.ui.phoneVerification.IPhoneVerificationListener
import kotlinx.android.synthetic.main.fragment_phone_verification_done.view.*


class PhoneVerificationDoneFragment : Fragment() {

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

        view.textDescriptionAnimationCheckout.text = "Congratulations"

        view.phoneDoneButton.setOnClickListener {
            listener.onCompleteVerification()
            Handler(Looper.getMainLooper()).removeCallbacks(callbacks)
        }

        Handler(Looper.getMainLooper()).postDelayed(callbacks, 4000)
        return view
    }
}