package com.mx.crystalcloud.bookodemia.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mx.crystalcloud.bookodemia.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentView = findViewById(android.R.id.content)

        setValidationListener(til_email, tiet_email)
        setValidationListener(til_password, tiet_password)

        btn_login.setOnClickListener {
            val email: String = tiet_email.text?.trim().toString()
            val pass: String = tiet_password.text?.trim().toString()
            var ok: Boolean = true;
            if (email.isEmpty()) {
                til_email.error = applicationContext.resources.getString(R.string.require_field)
                ok = false;
            } else {
                til_email.error = null
            }
            if (pass.isEmpty()) {
                til_password.error = applicationContext.resources.getString(R.string.require_field)
                ok = false;
            } else {
                til_password.error = null
            }
            if (ok) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val userEmail = prefs.getString("email", "NaN")
                val userPass = prefs.getString("password", "NaN")

                if (userEmail == "NaN" || userPass == "NaN") {
                    showMessage(applicationContext.resources.getString(R.string.wrong_user_data))
                } else {
                    if (userEmail == email && userPass == pass) {
                        startActivity(Intent(this, Home::class.java))
                    } else {
                        showMessage(applicationContext.resources.getString(R.string.wrong_user_data))
                    }
                }
            }
        }

        tv_create_account.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun setValidationListener(til: TextInputLayout, tiet: TextInputEditText) {
        tiet.addTextChangedListener(object : TextWatcher {
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

    private fun showMessage(message:String) {
        Snackbar.make(
            parentView!!,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}