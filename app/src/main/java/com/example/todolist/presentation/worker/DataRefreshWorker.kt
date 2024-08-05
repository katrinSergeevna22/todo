package com.example.todolist.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.data.repository.RemoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class DataRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: RemoteRepository
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                repository.getTasks()
                //ViewModel().fetchRepository()
                Result.success()
            } catch (ex: Exception) {
                Result.retry()
            }
        }
    }
}