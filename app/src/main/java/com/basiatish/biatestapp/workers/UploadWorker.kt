package com.basiatish.biatestapp.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UploadWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    private val storage = Firebase.storage("gs://biatestapp.appspot.com")
    private val storageRef = storage.reference

    override suspend fun doWork(): Result {
        val fileName = inputData.getStringArray("files")
        val user = inputData.getString("user")
        val taskID = inputData.getString("taskID")
        fileName?.forEach { path ->
            val uri = Uri.parse(path)
            val fileRef = storageRef.child("users/$user/documents/$taskID/${uri.lastPathSegment}")
            withContext(Dispatchers.IO) {
                fileRef.putFile(uri)
            }
        }
        return Result.success()
    }
}