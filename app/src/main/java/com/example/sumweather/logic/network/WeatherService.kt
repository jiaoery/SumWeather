package com.example.sumweather.logic.network

import com.example.sumweather.SunnyWeatherApplication
import com.example.sumweather.logic.model.DailyResponse
import com.example.sumweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{late}/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng:String, @Path("lat")lat:String): Call<RealTimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{late}/daily.json")
    fun getDailyWeather(@Path("lng")lng:String, @Path("lat")lat:String): Call<DailyResponse>
}