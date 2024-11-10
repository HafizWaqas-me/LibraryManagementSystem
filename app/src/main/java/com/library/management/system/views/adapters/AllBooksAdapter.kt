package com.library.management.system.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.library.management.system.R
import com.library.management.system.model.CardClick
import com.library.management.system.model.Books

class AllBooksAdapter(
    val context: Context,
    var list: List<Books>,
    val listner: CardClick?
) :
    RecyclerView.Adapter<AllBooksAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val bookImg: ImageView = v.findViewById(R.id.book_img)
        val bookTitle: TextView = v.findViewById(R.id.book_title)
        val author: TextView = v.findViewById(R.id.book_author)
        val category: TextView = v.findViewById(R.id.book_category)
        val card: CardView = v.findViewById(R.id.card_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.books_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].imgUrl)
            .centerCrop()
            .placeholder(R.drawable.loading_img)
            .into(holder.bookImg)

        holder.bookTitle.text = list[position].title
        holder.category.text = list[position].category
        holder.author.text = list[position].author

        holder.card.setOnClickListener {
            listner?.onCardClick(list[position].bookId)

            Log.d("AllBookAdapterr", "${list[position].bookId}")
        }
    }


    fun filterList(filteredCourseList: MutableList<Books>) {
        this.list = filteredCourseList
        notifyDataSetChanged()
    }
    fun updateList(newList:List<Books>){
        list = newList
        notifyDataSetChanged()
    }
}