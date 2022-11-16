package ru.technostore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TechnoStoreApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}