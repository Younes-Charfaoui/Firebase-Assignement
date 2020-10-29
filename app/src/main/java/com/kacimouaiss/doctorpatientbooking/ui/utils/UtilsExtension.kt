@file:Suppress("unused")

package com.kacimouaiss.doctorpatientbooking.ui.utils

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import es.dmoral.toasty.Toasty


fun TextView.makeLinks(link: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            link.second.onClick(view)
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(link.first)
    spannableString.setSpan(
        clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun AppCompatActivity.fragmentTransition(
    container: Int,
    fragment: Fragment,
    addToStack: Boolean = false,
    tag: String? = null
) {
    val fragmentOld = supportFragmentManager.findFragmentByTag(tag)
    supportFragmentManager.commit {
        replace(container, fragmentOld ?: fragment, tag)
        if (addToStack) addToBackStack(null)
        setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
    }
}

fun AppCompatActivity.switchToPrevious(
    tag: String,
    container: Int
) {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    if (fragment != null)
        supportFragmentManager.commit {
            replace(container, fragment, tag)
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        }
}

fun Context.toastError(string: String) {
    Toasty.error(this, string).show()
}

fun Context.toastError(string: Int) {
    Toasty.error(this, string).show()
}

fun Context.toastInfo(string: String) {
    Toasty.info(this, string).show()
}

fun Context.toastInfo(string: Int) {
    Toasty.info(this, string).show()
}

fun Context.toastSuccess(string: String) {
    Toasty.success(this, string).show()
}

fun Context.toastSuccess(string: Int) {
    Toasty.success(this, string).show()
}