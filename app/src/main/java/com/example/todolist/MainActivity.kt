package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, ListFragment.newInstance())
            .commit()
    }
}