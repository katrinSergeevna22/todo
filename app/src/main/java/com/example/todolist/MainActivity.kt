package com.example.todolist

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todolist.domain.repository.ISettingRepository
//import androidx.activity.compose.setContent
import com.example.todolist.presentation.ui.compose.navigation.AppNavigation
import com.example.todolist.presentation.ui.theme.ToDoListComposeTheme
import com.example.todolist.presentation.viewModel.ListViewModel
import com.example.todolist.presentation.worker.DataRefreshWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application main activity
 **/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListComposeTheme {
                AppNavigation()
            }
        }

        val refreshWorker = PeriodicWorkRequestBuilder<DataRefreshWorker>(8, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueue(refreshWorker)
        observeNetworkConnectivity()
    }

    @Inject
    lateinit var settingRepository: ISettingRepository

    private val listViewModel: ListViewModel by viewModels()

    private fun observeNetworkConnectivity() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                settingRepository.setNotificationEnabled(true)
                listViewModel.updateTasks()
            }

            override fun onLost(network: Network) {
                settingRepository.setNotificationEnabled(false)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
}


/* View
class MainActivity : AppCompatActivity() {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, ListFragment.newInstance())
            .commit()
    }


}

 */