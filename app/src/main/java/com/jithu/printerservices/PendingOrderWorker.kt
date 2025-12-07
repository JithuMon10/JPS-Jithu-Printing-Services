package com.jithu.printerservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.room.Room
import java.time.LocalTime

class PendingOrderWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "jvs-db").build()
        val pendingCount = db.orderDao().getAll().count { !it.completed }
        if (pendingCount > 0) {
            sendNotification(pendingCount)
        }
        return Result.success()
    }

    private fun sendNotification(pendingCount: Int) {
        val channelId = "pending_orders_channel"
        val notificationId = 2001
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Order Reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notifies you of pending orders."
            }
            nm.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Pending Orders")
            .setContentText("You have $pendingCount pending print orders.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }
}
