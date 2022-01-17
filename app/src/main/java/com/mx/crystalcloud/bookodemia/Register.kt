package com.mx.crystalcloud.bookodemia

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.tiet_email
import kotlinx.android.synthetic.main.activity_register.tiet_password
import kotlinx.android.synthetic.main.activity_register.til_email
import kotlinx.android.synthetic.main.activity_register.til_password
import java.util.regex.Matcher
import java.util.regex.Pattern

class Register : AppCompatActivity() {

    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        parent_view = findViewById(android.R.id.content)

        setValidationListener(til_user, tiet_user)
        setValidationListener(til_email, tiet_email)
        setValidationListener(til_password, tiet_password)
        setValidationListener(til_password_c, tiet_password_c)

        btn_signin.setOnClickListener {
            val user: String = tiet_user.text?.trim().toString()
            val email: String = tiet_email.text?.trim().toString()
            val pass: String = tiet_password.text?.trim().toString()
            val passConfirm: String = tiet_password_c.text?.trim().toString()
            var ok:Boolean = true;
            if (user.isEmpty()) {
                til_user.error = applicationContext.resources.getString(R.string.require_field)
                ok = false;
            } else {
                til_user.error = null
            }
            if (email.isEmpty()) {
                til_email.error = applicationContext.resources.getString(R.string.require_field)
                ok = false;
            } else {
                if (isEmail(email)) {
                    til_email.error = null
                } else {
                    til_email.error = applicationContext.resources.getString(R.string.wrong_email)
                    ok = false;
                }
            }
            if (pass.isEmpty()) {
                til_password.error = applicationContext.resources.getString(R.string.require_field)
                if (passConfirm.isEmpty()) {
                    til_password_c.error = applicationContext.resources.getString(R.string.require_field)
                    ok = false;
                } else {
                    til_password_c.error = null
                }
            } else {
                til_password.error = null
                if (passConfirm.isEmpty()) {
                    til_password_c.error = applicationContext.resources.getString(R.string.require_field)
                    ok = false;
                } else {
                    if (pass == passConfirm) {
                        til_password_c.error = null
                    } else {
                        til_password_c.error = applicationContext.resources.getString(R.string.dif_password)
                        ok = false;
                    }
                }
            }
            if (ok) {
                saveData(user, email, pass)
            }
        }

        tv_back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun saveData(uName:String, uEmail:String, uPassword:String) {
        val user = User().apply {
            name = uName.lowercase()
            email = uEmail.lowercase()
            password = uPassword
        }.also {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val uKey = "username";
            val uPass = "password"
            val uEmail = "email"
            val storedUser = prefs.getString(uKey, "NaN")
            val editor = prefs.edit()
            editor.putString(uKey, it.name)
            editor.putString(uPass, it.password)
            editor.putString(uEmail, it.email)
            editor.apply()
            if (storedUser == "NaN") {
                Snackbar.make(parent_view!!,"Usuario ${it.name} creado con éxito",Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(parent_view!!,"Usuario ${it.name} modificado con éxito",Snackbar.LENGTH_SHORT).show()
            }
            startActivity(Intent(this, MainActivity::class.java))
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

    fun isEmail(text:String):Boolean{
        var pattern:Pattern=Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        var comparador:Matcher=pattern.matcher(text)
        return comparador.find()
    }

}