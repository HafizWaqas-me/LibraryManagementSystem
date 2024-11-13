package com.library.management.system.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.library.management.system.R
import com.library.management.system.utils.OVERDUE_CHANNEL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val expiryDate = inputData.getString("expiryDate") ?: return Result.failure()
        val bookId = inputData.getString("bookId") ?: return Result.failure()
        val isOverdue = inputData.getBoolean("isOverdue", false)

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val returnDate = LocalDate.parse(expiryDate, dateFormat)
        val today = LocalDate.now()

        val notificationMessage = if (isOverdue) {
            val daysOverdue = ChronoUnit.DAYS.between(returnDate, today).toInt()
            val fine = daysOverdue * DAILY_FINE
            "Your book $bookId is $daysOverdue days overdue. Current fine: $fine."
        } else {

            "Your book$bookId is due tomorrow. Please return it to avoid fines."
        }

        showFineNotification("Library Alert", notificationMessage, bookId.hashCode())
        return Result.success()
    }

    companion object {
        const val DAILY_FINE = 50
    }

    fun showFineNotification(title: String, message: String, notificationId: Int) {

        val notification = NotificationCompat.Builder(applicationContext, OVERDUE_CHANNEL)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_book)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .build()

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
