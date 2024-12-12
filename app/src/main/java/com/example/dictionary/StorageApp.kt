package com.example.dictionary

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room

class StorageApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        passwordStorage = PasswordStorage(applicationContext)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "diary"
        ).build()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var passwordStorage: PasswordStorage
        lateinit var db: AppDatabase

        lateinit var instance: StorageApp
            private set
    }
}