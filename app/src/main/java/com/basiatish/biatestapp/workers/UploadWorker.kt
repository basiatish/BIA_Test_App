package com.basiatish.biatestapp.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.basiatish.biatestapp.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext


class UploadWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    private val storage = Firebase.storage("gs://biatestapp.appspot.com")
    private val storageRef = storage.reference
    private var status = ""
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "12"
    private var count = 0
    private var size = 0

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
    override suspend fun doWork(): Result {
        val fileName = inputData.getStringArray("files")
        val user = inputData.getString("user")
        val taskID = inputData.getString("taskID")
        val size = fileName?.size
        fileName?.forEach { path ->
            val uri = Uri.parse(path)
            val fileRef = storageRef.child("users/companies/$user/documents/$taskID/${uri.lastPathSegment}")
            val uploadTask = fileRef.putFile(uri)
            val task = Tasks.await(uploadTask)
            count++
            setForeground(createForegroundInfo())
            if (count == size) {
                status = applicationContext.getString(R.string.documents_toast)
                setForeground(createForegroundInfo())
            }
        }
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val title = applicationContext.getString(R.string.loaded)

        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(status)
            .setSmallIcon(R.drawable.bia_logo)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    private fun createNotification() : Notification {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.bia_logo)
            .setContentTitle(applicationContext.getString(R.string.loaded))
            .setContentText(status)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    private fun createNotificationChannel() {
        val name = "App Notification Manager"
        val descriptionText = "Shows notifications whenever documents loaded"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}