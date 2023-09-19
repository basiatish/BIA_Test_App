package com.basiatish.biatestapp.workers

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File


class DownLoadWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {

    private val storage = Firebase.storage("gs://biatestapp.appspot.com")
    private val storageRef = storage.reference

    override suspend fun doWork(): Result {
        val fileName = inputData.getString("fileName")
        val user = inputData.getString("user")
        val taskID = inputData.getString("taskID")
        val licenceRef = storageRef.child("users/$user/licences/$taskID")
        val fileRef = licenceRef.child(fileName!!)
        withContext(Dispatchers.IO) {
            download(fileRef)
        }
        return Result.success()
    }

    private suspend fun download(fileRef: StorageReference){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "licence.pdf")
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = applicationContext.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { ops ->
                    fileRef.stream.await().stream.use { ips ->
                        val buffer = ByteArray(4096)
                        while (true) {
                            val bytes = ips.read(buffer)
                            if (bytes == -1)
                                break
                            ops.write(buffer, 0, bytes)
                        }
                    }
                }
                resolver.update(uri, contentValues, null, null)
            }
        } else {
            val outputFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "licence.pdf")
            fileRef.getFile(outputFile)
        }
    }
}