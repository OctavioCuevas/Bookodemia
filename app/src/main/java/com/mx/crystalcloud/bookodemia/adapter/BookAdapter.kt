package com.mx.crystalcloud.bookodemia.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mx.crystalcloud.bookodemia.Book
import com.mx.crystalcloud.bookodemia.R
import com.mx.crystalcloud.bookodemia.model.BookData

class BookAdapter(val books: MutableList<BookData>) :
    RecyclerView.Adapter<BookAdapter.BookHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BookHolder(layoutInflater.inflate(R.layout.item_cv_book, parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookHolder, position: Int) {
        holder.render(books[position])
    }

    override fun getItemCount(): Int = books.size

    class BookHolder(val view: View, context: Context) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.cv_book)
        val textViewTitle: TextView = view.findViewById(R.id.tv_title)
        val textViewAuthor: TextView = view.findViewById(R.id.tv_author)
        val textViewCategory: TextView = view.findViewById(R.id.tv_category)
        val imageViewBook: ImageView = view.findViewById(R.id.iv_book)
        val butonBook : Button = view.findViewById(R.id.btn_book)

        fun render(book: BookData) {
            textViewTitle.setText(book.title)
            textViewAuthor.setText(book.author)
            textViewCategory.setText(book.category)
            imageViewBook.setImageResource(R.drawable.book_2)
            cardView.setOnClickListener {
                it.context.startActivity(Intent(it.context,Book::class.java))
            }

            butonBook.setOnClickListener {
                it.context.startActivity(Intent(it.context,Book::class.java))
            }
        }


    }
}