package com.example.sumweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun searchPlace(query: String) = placeService.searchPlace(query).await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private val weatherNetWork = ServiceCreator.create(WeatherService::class.java)

    suspend fun getDailyWeather(lng:String,lat:String) = weatherNetWork.getDailyWeather(lng,lat).await()

    suspend fun getRealtimeWeather(lng:String,lat:String) = weatherNetWork.getRealtimeWeather(lng,lat).await()
}