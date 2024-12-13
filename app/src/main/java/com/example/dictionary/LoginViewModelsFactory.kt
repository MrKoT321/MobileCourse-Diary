package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelsFactory(private val storage: PasswordStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storage) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}