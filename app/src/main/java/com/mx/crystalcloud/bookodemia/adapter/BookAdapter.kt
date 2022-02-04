package com.mx.crystalcloud.bookodemia.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mx.crystalcloud.bookodemia.Book
import com.mx.crystalcloud.bookodemia.R
import com.mx.crystalcloud.bookodemia.model.Item
import java.io.Serializable

class BookAdapter(
    private val books: MutableList<Item>,
    //val categories: MutableList<Item>,
    //val authors: MutableList<Item>,
    private val activity: Activity
) :
    RecyclerView.Adapter<BookAdapter.BookHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BookHolder(
            layoutInflater.inflate(R.layout.item_cv_book, parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: BookAdapter.BookHolder, position: Int) {
        holder.render(books[position])
        val book = books.get(position)
        //val cat = categories.get(position)
        //val author = authors.get(position)
        with(holder) {
            cardView.setOnClickListener {
                openBook(
                    book
                    //, cat, author
                )
            }
            butonBook.setOnClickListener {
                openBook(
                    book
                    //, cat, author
                )
            }
        }
    }

    private fun openBook(item: Serializable
                         //, cat: Serializable, author: Serializable
        ) {
        val bundle = Bundle()
        bundle.putSerializable("item", item)
        //bundle.putSerializable("category", cat)
        //bundle.putSerializable("author", author)
        val intent = Intent(activity, Book::class.java)
        intent.putExtras(bundle)
        activity.startActivity(intent)

    }

    override fun getItemCount(): Int = books.size

    class BookHolder(val view: View, context: Context) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.cv_book)
        val textViewTitle: TextView = view.findViewById(R.id.tv_title)
        val textViewAuthor: TextView = view.findViewById(R.id.tv_author)
        val textViewCategory: TextView = view.findViewById(R.id.tv_category)
        val imageViewBook: ImageView = view.findViewById(R.id.iv_book)
        val butonBook: Button = view.findViewById(R.id.btn_book)

        fun render(book: Item) {
            textViewTitle.setText(book.attributes.title)
            textViewAuthor.setText("Author")
            textViewCategory.setText("Category")
            imageViewBook.setImageResource(R.drawable.book_2)
        }
    }
}