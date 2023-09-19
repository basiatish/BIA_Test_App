package com.basiatish.biatestapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.basiatish.biatestapp.di.AppComponent
import com.basiatish.biatestapp.di.DaggerAppComponent
import com.basiatish.biatestapp.workers.DownLoadWorker
import com.basiatish.biatestapp.workers.UploadWorker

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    fun downLoad(name: String, taskID: Int) {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val data = Data.Builder()
            .putString("fileName", "test.pdf")
            .putString("user", name)
            .putString("taskID", taskID.toString())
            .build()
        val request = OneTimeWorkRequestBuilder<DownLoadWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observeForever {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Toast.makeText(applicationContext,
                    getString(R.string.rules_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

}