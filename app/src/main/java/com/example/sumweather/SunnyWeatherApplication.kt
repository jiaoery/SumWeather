package com.example.sumweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {

    companion object {
        lateinit var context: Context
        //获取到的令牌token
        const val TOKEN = "P3c4Ku50Mx01JEcx"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}