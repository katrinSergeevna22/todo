package com.example.todolist.presentation.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

/**
* Coroutine Worker, used for background to download data from the network
* @param context Application context
* @param workerParams Worker job parameters
 **/
class DataRefreshWorker1(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        //val d: ListViewModel = rememberViewModel().fetchRepository()
        return Result.success()
    }
}

fun startDataRefreshWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val dataRefreshRequest = PeriodicWorkRequestBuilder<DataRefreshWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DataRefresh",
        ExistingPeriodicWorkPolicy.REPLACE,
        dataRefreshRequest
    )
}
