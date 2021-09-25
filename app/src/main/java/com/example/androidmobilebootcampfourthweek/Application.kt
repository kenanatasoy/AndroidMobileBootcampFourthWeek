package com.example.androidmobilebootcampfourthweek

import android.app.Application
import com.example.androidmobilebootcampfourthweek.service.ServiceConnector

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceConnector.init()
    }
}