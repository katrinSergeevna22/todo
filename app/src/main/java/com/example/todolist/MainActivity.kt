package com.example.todolist

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.presentation.NetworkChangeReceiver
//import androidx.activity.compose.setContent
import com.example.todolist.presentation.ui.compose.navigation.AppNavigation
import com.example.todolist.presentation.ui.theme.ToDoListComposeTheme
import com.example.todolist.presentation.worker.startDataRefreshWorker

/**
 * Application main activity
 **/
class MainActivity : AppCompatActivity() {

    private val networkChangeReceiver = NetworkChangeReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListComposeTheme {
                AppNavigation()
            }
        }
        startDataRefreshWorker(this)
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

}
/*
val repository = TaskRepository()
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()

val myWorkRequest = PeriodicWorkRequestBuilder<DataRefreshWorker>(8, TimeUnit.HOURS)
    .setConstraints(constraints)
    .build()

WorkManager.getInstance(this).enqueue(myWorkRequest)

// Проверка доступности интернета и автоматическая загрузка данных
val connectivityManager =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
val networkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        //val settingRepository = SettingsParameters()
        // Отправить запрос на получение данных
        CoroutineScope(Dispatchers.IO).launch {
            repository.getTasksFlow()
            //repository.getTasks(settingRepository.getToken())
        }
    }
}

connectivityManager.registerDefaultNetworkCallback(networkCallback)
}
}
//        WorkManager.initialize(this, configurationWorker)
//        WorkManager.getInstance(this).enqueue(myWorkRequest)

 */
//    }

//}

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