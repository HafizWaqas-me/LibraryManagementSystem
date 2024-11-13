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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.library.management.system.R
import com.library.management.system.model.IssueBooks
import com.library.management.system.notifications.NotificationWorker
import com.library.management.system.viewmodels.UserDashBoardVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class UserAdapter(
    val context: Context,
    var list: List<IssueBooks>,
    val viewModel: UserDashBoardVM
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bookImg: ImageView = v.findViewById(R.id.book_img_user)
        val bookTitle: TextView = v.findViewById(R.id.book_title_user)
        val bookAuthor: TextView = v.findViewById(R.id.book_author_user)
        val bookExpiry: TextView = v.findViewById(R.id.book_expiry_user)
        var expiryPlaceHolder: TextView = v.findViewById(R.id.book_expiry_hint)
        val tvExpired: TextView = v.findViewById(R.id.tv_book_expired)
        val card: CardView = v.findViewById(R.id.user_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.user_item_view, parent, false)

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
        holder.bookExpiry.text = list[position].expiryDate.toString()

        val expiryDate = list[position].expiryDate.toString()

        if (expiryDate.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val returnDate: Date? = dateFormat.parse(expiryDate)

            val currentDate = Date()
            val differenceInTime = currentDate.time - (returnDate?.time ?: currentDate.time)
            val differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)).toInt()

            if (differenceInDays == -1) {
                scheduleNotification(list[position].bookId.toString(), expiryDate, false)
            } else if (differenceInDays > 0) {
                scheduleNotification(list[position].bookId.toString(), expiryDate, true)
                holder.expiryPlaceHolder.text = "Expired"
                holder.expiryPlaceHolder.setTextColor(context.resources.getColor(R.color.red))
                holder.bookExpiry.setTextColor(context.resources.getColor(R.color.red))

                val fine = differenceInDays * 50
                holder.tvExpired.visibility = View.VISIBLE
                holder.tvExpired.text = "Fine For This Book is: $fine"
                Log.d("Finee", "User is $differenceInDays days late. Fine: Rs. $fine")
            } else {
                Log.d("Finee", "No fine. The book is returned on time.")
            }
        } else {
            Log.d("Finee", "Book Not Found")
        }
    }

    fun updateList(newList: List<IssueBooks>) {
        list = newList
        notifyDataSetChanged()
    }

    private fun scheduleNotification(bookId: String, expiryDate: String, isOverdue: Boolean) {
        val workManager = WorkManager.getInstance(context)

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(10, TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    "expiryDate" to expiryDate,
                    "bookId" to bookId,
                    "isOverdue" to isOverdue
                )
            )
            .addTag(bookId)
            .build()

        workManager.enqueueUniquePeriodicWork(
            bookId,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }


}
