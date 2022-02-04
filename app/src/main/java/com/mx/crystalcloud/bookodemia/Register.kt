package com.mx.crystalcloud.bookodemia.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.mx.crystalcloud.bookodemia.R
import com.mx.crystalcloud.bookodemia.extra.*
import com.mx.crystalcloud.bookodemia.model.Errors
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.tiet_email
import kotlinx.android.synthetic.main.activity_register.tiet_password
import kotlinx.android.synthetic.main.activity_register.til_email
import kotlinx.android.synthetic.main.activity_register.til_password
import org.json.JSONObject

class Register : AppCompatActivity() {

    private var parentView: View? = null
    private val TAG = Register::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        parentView = findViewById(android.R.id.content)

        setValidationListener(til_user, tiet_user)
        setValidationListener(til_email, tiet_email)
        setValidationListener(til_password, tiet_password)
        setValidationListener(til_password_c, tiet_password_c)

        btn_signin.setOnClickListener {
            val user: String = tiet_user.text?.trim().toString()
            val email: String = tiet_email.text?.trim().toString()
            val pass: String = tiet_password.text?.trim().toString()
            val passConfirm: String = tiet_password_c.text?.trim().toString()
            if (fieldValidation("email", tiet_email, til_email, applicationContext)) {
                if (fieldValidation("name", tiet_user, til_user, applicationContext)) {
                    if (fieldValidation(
                            "password",
                            tiet_password,
                            til_password,
                            applicationContext
                        )
                    ) {
                        if (fieldValidation(
                                "password",
                                tiet_password_c,
                                til_password_c,
                                applicationContext
                            )
                        ) {
                            if (comparePassword(pass, passConfirm)) {
                                //saveData(user, email, pass)
                                newUserRequest(user, email, pass)
                            }
                        }
                    }
                }
            }
        }

        tv_back.setOnClickListener {
            super.onBackPressed();
        }
    }

    private fun saveData(uName: String, uEmail: String, uPassword: String) {
        val user = User().apply {
            name = uName.lowercase()
            email = uEmail.lowercase()
            password = uPassword
        }.also {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val uKey = "username"
            val uPass = "password"
            val uEmail = "email"
            val storedUser = prefs.getString(uKey, "NaN")
            val editor = prefs.edit()
            editor.putString(uKey, it.name)
            editor.putString(uPass, it.password)
            editor.putString(uEmail, it.email)
            editor.apply()
            if (storedUser == "NaN") {
                Snackbar.make(
                    parentView!!,
                    "Usuario ${it.name} creado con éxito",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    parentView!!,
                    "Usuario ${it.name} modificado con éxito",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            startActivity(Intent(this, MainActivity::class.java))
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

    private fun comparePassword(password: String, password_c: String): Boolean {
        return if (password == password_c) {
            til_password_c.error = null
            true
        } else {
            til_password_c.error =
                applicationContext.resources.getString(R.string.dif_password)
            false
        }
    }

    fun newUserRequest(name: String, email: String, password: String) {
        if (isOnline(applicationContext)) {
            val json = JSONObject()
            json.put("name", name)
            json.put("email", email)
            json.put("password", password)
            json.put("password_confirmation", password)
            json.put("device_name", android.os.Build.MODEL)
            val queue = Volley.newRequestQueue(applicationContext)

            val request = object : JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.url_server) + getString(R.string.api_register),
                json,
                { response ->
                    Log.d(TAG, response.toString())
                    val jsonObject = JSONObject(response.toString())
                    saveSesion(applicationContext, jsonObject)
                    val intent = Intent(this, Home::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                },
                { error ->
                    if (error.networkResponse.statusCode == 422) {
                        popUp(this, getString(R.string.error_422))
                    } else {
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        //val errors = Json.decodeFromString<Errors>(json.toString())
                        val errors = Gson().fromJson(json.toString(), Errors::class.java)
                        for (error in errors.errors) {
                            popUp(this, error.detail)
                        }
                    }
                    Log.e(TAG, error.toString())
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            queue.add(request)
        } else {
            popUp(this, getString(R.string.error_internet))
        }
    }

}