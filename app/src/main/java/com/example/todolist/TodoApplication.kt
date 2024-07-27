package com.example.todolist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {

    //    @Inject
//    lateinit var networkChangeReceiver: NetworkChangeReceiver
    override fun onCreate() {
        super.onCreate()

//        registerReceiver(
//            networkChangeReceiver,
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        )
//
//        startDataRefreshWorker(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        //unregisterReceiver(networkChangeReceiver)
    }
}