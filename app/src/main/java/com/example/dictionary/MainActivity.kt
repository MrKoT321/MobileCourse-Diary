package com.example.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dictionary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val diaryViewModel: DiaryViewModel by lazy {
        val factory = DiaryViewModelsFactory(StorageApp.db.recordDao())
        val provider = ViewModelProvider(owner = this, factory)

        provider[DiaryViewModel::class.java]
    }

    private val loginViewModel: LoginViewModel by lazy {
        val factory = LoginViewModelsFactory(StorageApp.passwordStorage)
        val provider = ViewModelProvider(owner = this, factory)

        provider[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        println(diaryViewModel)
        println(loginViewModel)
    }
}