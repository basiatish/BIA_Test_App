package com.basiatish.biatestapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import java.lang.StringBuilder

class CodeTextWatcher(
    private val input: EditText,
    private val button: AppCompatButton
) : TextWatcher {
    private var lastChar = ""
    private var lastSize = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        lastSize = input.text.toString().length
        if (lastSize == 0) lastChar = ""
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val currentSize = input.text.toString().length
        if (currentSize > 1) {
            lastChar = input.text.toString().substring(currentSize - 1)
        }
        if (currentSize > lastSize) {
            if (lastChar != " ") {
                input.append("  ")
            }
        }
        button.isEnabled = input.text.toString().length == 16
    }

    override fun afterTextChanged(s: Editable) {

    }

}