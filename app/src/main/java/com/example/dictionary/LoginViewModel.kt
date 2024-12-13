package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storage: PasswordStorage
) : ViewModel() {
    private var passwordToComplete = ""
    val password = MutableStateFlow("")
    val wasIncorrectPassword = MutableStateFlow(false)
    val completeLogin = MutableStateFlow(false)
    val loginTitleText = MutableStateFlow("")

    init {
        viewModelScope.launch {
            passwordToComplete = storage.getPin() ?: ""
            loginTitleText.value =
                if (passwordToComplete.isEmpty()) "Придумайте PIN-код" else "Введите PIN-код"
        }
    }

    fun handle(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnterEvent -> {
                if (!completeLogin.value) {
                    updatePassword(event.value)
                }
            }
        }
    }

    private fun updatePassword(value: String) {
        password.value += value
        if (password.value.length >= MAX_PASSWORD_LEN) {
            if (passwordToComplete.isEmpty()) {
                passwordToComplete = password.value.slice(0..<MAX_PASSWORD_LEN)
                loginTitleText.value = "Повторите PIN-код"
            } else {
                if (passwordToComplete == password.value) {
                    viewModelScope.launch {
                        storage.savePin(passwordToComplete)
                    }
                    completeLogin.value = true
                } else {
                    wasIncorrectPassword.value = true
                }
            }
            password.value = ""
        }
    }

    companion object {
        private const val MAX_PASSWORD_LEN = 4
    }
}