package com.mx.crystalcloud.bookodemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValidationListener(til_email, tiet_email)
        setValidationListener(til_password, tiet_password)

        btn_login.setOnClickListener {
            val email: String = tiet_email.text?.trim().toString()
            val pass: String = tiet_password.text?.trim().toString()
            if (email.isEmpty()) {
                til_email.error = applicationContext.resources.getString(R.string.require_field)
            } else {
                til_email.error = null
            }
            if (pass.isEmpty()) {
                til_password.error = applicationContext.resources.getString(R.string.require_field)
            } else {
                til_password.error = null
            }
        }
    }

    private fun setValidationListener(til: TextInputLayout, tiet: TextInputEditText) {
        tiet.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(txt: Editable?) {
                if (txt.toString().isEmpty()) {
                    til.error = applicationContext.resources.getString(R.string.require_field)
                } else {
                    til.isErrorEnabled = false
                    til.error = ""
                }
            }
        })
    }
}