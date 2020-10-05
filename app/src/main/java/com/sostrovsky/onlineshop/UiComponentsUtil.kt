package com.sostrovsky.onlineshop

import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.widget.TextView

object UiComponentsUtil {
    fun strikeLineThrough(textView: TextView, text: String) {
        textView.apply {
            setText(text, TextView.BufferType.SPANNABLE)

            val spannable = textView.text as Spannable
            spannable.setSpan(
                StrikethroughSpan(), 0, text.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }
}