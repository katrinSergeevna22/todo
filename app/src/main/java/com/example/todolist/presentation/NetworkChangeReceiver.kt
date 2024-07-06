package com.example.todolist.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.todolist.presentation.viewModel.ListViewModelProvider
/**
 * Broadcast monitoring the Internet connection
 * */
class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkAvailable(context)) {
            ListViewModelProvider.listViewModel.fetchRepository()
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
