package com.example.sumweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {

    companion object {
        lateinit var context: Context
        //获取到的令牌token
        const val TOKEN = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}