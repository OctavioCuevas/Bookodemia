package com.mx.crystalcloud.bookodemia.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.mx.crystalcloud.bookodemia.R
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        parentView = findViewById(android.R.id.content)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val userName = prefs.getString("username", "NaN")
        tv_username.setText(userName)

        //Modificar cuando se cree la Activity de Detalle de Libro
        iv_book_1.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        iv_book_2.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        iv_book_3.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        iv_book_4.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        iv_book_5.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

    }
}