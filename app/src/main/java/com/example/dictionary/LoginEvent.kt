package com.example.dictionary

sealed class LoginEvent {
    data class EnterEvent(val value: String) : LoginEvent()
}