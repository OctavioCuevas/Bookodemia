package com.mx.crystalcloud.bookodemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mx.crystalcloud.bookodemia.model.Item
import kotlinx.android.synthetic.main.activity_book.*

class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        tv_back.setOnClickListener(){
            super.onBackPressed();
        }

        intent.extras?.let{
            val book = it.getSerializable("book") as Item
            tv_book_title.text = book.attributes.title
            tv_book_author.text = it.getString("id")
        }
    }
}