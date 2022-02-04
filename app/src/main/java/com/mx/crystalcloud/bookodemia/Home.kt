package com.mx.crystalcloud.bookodemia.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.mx.crystalcloud.bookodemia.Book
import com.mx.crystalcloud.bookodemia.R
import com.mx.crystalcloud.bookodemia.adapter.BookAdapter
import com.mx.crystalcloud.bookodemia.extra.dropSession
import com.mx.crystalcloud.bookodemia.extra.getSession
import com.mx.crystalcloud.bookodemia.extra.isOnline
import com.mx.crystalcloud.bookodemia.extra.popUp
import com.mx.crystalcloud.bookodemia.model.*
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class Home : AppCompatActivity() {

    private var parentView: View? = null
    private val TAG = Register::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        parentView = findViewById(android.R.id.content)

        getItems()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val userName = prefs.getString("username", "NaN")
        tv_back.setText(userName)

        //Modificar cuando se cree la Activity de Detalle de Libro
        iv_book_1.setOnClickListener {
            startActivity(Intent(this, Book::class.java))
        }
        iv_book_2.setOnClickListener {
            startActivity(Intent(this, Book::class.java))
        }
        iv_book_3.setOnClickListener {
            startActivity(Intent(this, Book::class.java))
        }
        iv_book_4.setOnClickListener {
            startActivity(Intent(this, Book::class.java))
        }
        iv_book_5.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun getRequest(type: String, params: String): MutableList<Item> {
        val list: MutableList<Item> = mutableListOf()
        if (isOnline(applicationContext)) {
            val queue = Volley.newRequestQueue(applicationContext)
            val request = object : JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.url_server) + params,
                null,
                { response ->
                    Log.d(TAG, response.toString())
                    val json = Gson().fromJson(response.toString(), Data::class.java)
                    for (item_data in json.data) {
                        Log.e(TAG, item_data.attributes.slug)
                        list.add(
                            Item(
                                item_data.type,
                                item_data.id,
                                item_data.attributes,
                                item_data.relationships,
                                item_data.links
                            )
                        )
                    }

                },
                { error ->
                    if (error.networkResponse.statusCode == 401) {
                        dropSession(applicationContext)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else if (error.networkResponse.statusCode == 429) {
                        getRequest(type, params)
                    } else {
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        //val errors = Json.decodeFromString<Errors>(json.toString())
                        val errors = Gson().fromJson(json.toString(), Errors::class.java)
                        for (error in errors.errors) {
                            popUp(this, error.detail)
                        }
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] =
                        "Bearer ${getSession(applicationContext, "token")}"
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            queue.add(request)
        } else {
            popUp(this, getString(R.string.error_internet))
        }
        return list
    }

    private fun getItems() {
        val books: MutableList<Item> =
            getRequest(getString(R.string.api_books), getString(R.string.api_books))
        /*val categories: MutableList<Item> = mutableListOf()
        val authors: MutableList<Item> = mutableListOf()*/
        for (book in books) {
            Log.e(TAG,book.attributes.slug)
            var req = getRequest(
                getString(R.string.api_categories),
                getString(R.string.api_books) + "/" + book.id + "/" + getString(
                    R.string.api_categories
                )
            )
           /* categories.add(req.get(0))
            req = getRequest(
                getString(R.string.api_authors),
                getString(R.string.api_books) + "/" + book.id + "/" + getString(
                    R.string.api_authors
                )
            )
            authors.add(req.get(0))*/
        }
        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.setHasFixedSize(true)
        var adapterBook = BookAdapter(books,// categories, authors,
             this)
        println(adapterBook)
        rv_books.adapter = adapterBook
    }
}