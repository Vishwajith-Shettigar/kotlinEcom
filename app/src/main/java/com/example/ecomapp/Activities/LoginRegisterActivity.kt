package com.example.ecomapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecomapp.R
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginregister)
    }
}