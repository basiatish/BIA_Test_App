package com.basiatish.biatestapp.utils

import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.basiatish.biatestapp.R
import java.lang.StringBuilder

class PhoneTextWatcher(
    private val input: EditText,
    private val hint: TextView,
    private val button: AppCompatButton
): TextWatcher {
    private var lastChar = ""
    private var lastSize = 0
    private val defaultPattern = "+7 (999) 000 - 00 - 00"

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        lastSize = input.text.toString().length
        val currentSize = input.text.toString().length
        if (currentSize > 1) {
            lastChar = input.text.toString().substring(currentSize - 1)
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val currentSize = input.text.toString().length
        if (currentSize > lastSize) {
            if (lastChar != " ") {
                if (currentSize == 2) {
                    input.append(" (")
                }
                if (currentSize == 7) {
                    input.append(") ")
                }
            }
            if (currentSize == 12 || currentSize == 17) {
                input.append(" - ")
            }
        } else {
            if (currentSize == 0) input.append("+7")
        }
        hint.text = StringBuilder().append(s).append(defaultPattern
            .subSequence(input.text.toString().length, defaultPattern.length))
        button.isEnabled = input.text.toString().length == 22
    }

    override fun afterTextChanged(s: Editable) {}

}