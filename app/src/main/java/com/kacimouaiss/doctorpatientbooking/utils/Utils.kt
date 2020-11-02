package com.kacimouaiss.doctorpatientbooking.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.kacimouaiss.doctorpatientbooking.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


object Utils {

    private const val TERMS_AND_CONDITION_URL = "https://www.na9ili.com/termes-et-conditions"
    private const val NA9ILI_WEBSITE_URL = "https://www.na9ili.com"
    private const val PRIVACY_POLICY_URL = "https://www.na9ili.com/privacy-policy"

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun checkInternetConnection(
        context: Context,
        onFailure: () -> Unit = { defaultNoInternet(context) },
        onSuccess: () -> Unit
    ) {
        if (isNetworkConnected(context)) {
            onSuccess()
        } else {
            onFailure()
        }
    }

    private fun defaultNoInternet(context: Context) {
        context.toastError("R.string.general_nointernet_message")
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun launchWebBrowserActivity(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null)
            context.startActivity(intent)
        else {
            context.toastError("R.string.general_toast_msg_no_browser")
        }
    }

    fun launchTermsAndCondition(context: Context) {
        launchWebBrowserActivity(context, TERMS_AND_CONDITION_URL)
    }

    fun launchAboutUs(context: Context) {
        launchWebBrowserActivity(context, NA9ILI_WEBSITE_URL)
    }

    fun launchPrivacyPolicy(context: Context) {
        launchWebBrowserActivity(context, PRIVACY_POLICY_URL)
    }

    fun sendFeedbackEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@na9ili.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Commentaires")
        if (intent.resolveActivity(context.packageManager) != null)
            context.startActivity(intent)
        else {
            context.toastError("R.string.general_toast_msg_no_email_app")
        }
    }

    fun sendInvoiceEmail(context: Context, imageFileUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("partenaire@na9ili.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Le Reçu")
        intent.type = "application/octet-stream"
        intent.putExtra(Intent.EXTRA_STREAM, imageFileUri)
        context.startActivity(Intent.createChooser(intent, "Envoi d'un e-mail..."))
    }

    fun formatDate(date: Date): String {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        return sdf.format(date)
    }

    fun formatDateAndTime(date: Date): String {
        val myFormat = "dd/MM/yyyy HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        return sdf.format(date)
    }

    fun getCircleColor(context: Context?): Int {

        val color = when (Random.nextBoolean()) {
            true -> R.color.colorPrimary
            else -> R.color.colorAccent
        }
        return ContextCompat.getColor(context!!, color)
    }
}