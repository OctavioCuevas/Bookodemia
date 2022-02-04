package com.mx.crystalcloud.bookodemia.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.mx.crystalcloud.bookodemia.R
import com.mx.crystalcloud.bookodemia.extra.*
import com.mx.crystalcloud.bookodemia.model.Errors
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var parentView: View? = null
    private val TAG = Register::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentView = findViewById(android.R.id.content)
        if (sessionValidator(applicationContext)) {
            openActivity()
        } else {
            tiet_email.setText("nel")
        }

        //tiet_password.setText(decrypt(applicationContext,"[B@f27a166".toByteArray()).toString())

        setValidationListener(til_email, tiet_email)
        setValidationListener(til_password, tiet_password)

        btn_login.setOnClickListener {
            if (fieldValidation("email", tiet_email, til_email, applicationContext)) {
                if (fieldValidation("password", tiet_password, til_password, applicationContext)) {
                    val email: String = tiet_email.text?.trim().toString()
                    val pass: String = tiet_password.text?.trim().toString()
                    loginRequest(email, pass)
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

    private fun showMessage(message: String) {
        Snackbar.make(
            parentView!!,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun loginRequest(user: String, password: String) {
        VolleyLog.DEBUG = true
        if (isOnline(applicationContext)) {
            val queue = Volley.newRequestQueue(applicationContext)
            val json = JSONObject()
            json.put("email", user)
            json.put("password", password)
            json.put("device_name", android.os.Build.MODEL)
            val request = object : JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.url_server) + getString(R.string.api_login),
                json,
                { response ->
                    Log.d(TAG, response.toString())
                    val jsonObject = JSONObject(response.toString())
                    saveSesion(applicationContext, jsonObject)
                    val intent = Intent(this, Home::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    if (sessionValidator(applicationContext)) {
                        openActivity()
                    }
                    startActivity(intent)
                    finish()
                },
                { error ->
                    val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                    val errors = Gson().fromJson(json.toString(), Errors::class.java)
                    for (error in errors.errors) {
                        popUp(this, error.detail)
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

    fun openActivity() {
        val intent = Intent(applicationContext, Home::class.java)
        startActivity(intent)
        finish()
    }
}