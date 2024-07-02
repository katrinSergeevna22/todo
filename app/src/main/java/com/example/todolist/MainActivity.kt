package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.presentation.ui.ListFragment
import com.example.todolistcompose.ui.MainScreen
import com.example.todolist.ui.theme.ToDoListComposeTheme

//import com.example.todolist.presentation.ui.TodoItemCard
//import com.example.todolist.presentation.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListComposeTheme {
                MainScreen()

            }
        }
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