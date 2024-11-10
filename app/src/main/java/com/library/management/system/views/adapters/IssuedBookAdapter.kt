package com.library.management.system.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.library.management.system.R
import com.library.management.system.model.IssueBooks
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IssuedBookAdapter(
    val context: Context,
    var list: List<IssueBooks>
) : RecyclerView.Adapter<IssuedBookAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bookImg: ImageView = v.findViewById(R.id.book_img_admin)
        val bookTitle: TextView = v.findViewById(R.id.book_title_admin)
        val bookAuthor: TextView = v.findViewById(R.id.book_author_admin)
        val userId: TextView = v.findViewById(R.id.tv_userId_admin)
        val tvExpired: TextView = v.findViewById(R.id.tv_expired)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.admin_item_view, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(list[position].imgUrl)
            .placeholder(R.drawable.loading_img)
            .into(holder.bookImg)
        holder.bookTitle.text = list[position].title
        holder.bookAuthor.text = list[position].author
        holder.userId.text = list[position].userId.toString()
        bookExpired(list[position].expiryDate.toString(), holder.tvExpired)
        Log.d("DATE_Fetched", "${list[position].expiryDate}")
    }

    fun filterList(filteredCourseList: MutableList<IssueBooks>) {
        this.list = filteredCourseList
        notifyDataSetChanged()
    }

    fun bookExpired(expiryDate: String, tvExpired: TextView) {
        if (expiryDate != null) {
            if (expiryDate.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val returnDate: Date? = dateFormat.parse(expiryDate)

                val currentDate = Date()

                val differenceInTime =
                    currentDate.time - (returnDate?.time ?: currentDate.time)
                val differenceInDays =
                    (differenceInTime / (1000 * 60 * 60 * 24)).toInt()

                if (differenceInDays > 0) {
                    tvExpired.visibility = View.VISIBLE
                    val fine = differenceInDays * 50
                    Log.d(
                        "Finee",
                        "User is $differenceInDays days late. Fine: Rs. $fine"
                    )
                } else {
                    Log.d("Finee", "No fine. The book is returned on time.")
                }
            }
        } else {
            Log.d("Finee", "Book Not Found")
        }
    }

    fun updateList(newList: List<IssueBooks>) {
        list = newList
        notifyDataSetChanged()
    }
}