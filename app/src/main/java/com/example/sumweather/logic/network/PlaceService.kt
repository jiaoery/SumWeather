package com.example.sumweather.logic.network

import com.example.sumweather.SunnyWeatherApplication
import com.example.sumweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh-CH")
    fun searchPlace(@Query("query") query: String): Call<PlaceResponse>
}
