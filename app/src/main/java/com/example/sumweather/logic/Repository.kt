package com.example.sumweather.logic

import androidx.lifecycle.liveData
import com.example.sumweather.logic.dao.PlaceDao
import com.example.sumweather.logic.model.Place
import com.example.sumweather.logic.model.Weather
import com.example.sumweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlace(query: String) = fire(Dispatchers.IO){
        val placeResponse = SunnyWeatherNetwork.searchPlace(query)
        if (placeResponse != null){
            val place = placeResponse.places
            Result.success(place)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng:String,lat:String) = fire(Dispatchers.IO){
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lat,lat)
                }
                val realTimeResponse = deferredRealtime.await()
                val dailyResponse =deferredDaily.await()
                if(realTimeResponse.status=="ok"&&dailyResponse.status == "ok"){
                    val weather = Weather(realTimeResponse.result.realTime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException("realtime response status is ${realTimeResponse.status}" +
                        " daily response is ${dailyResponse.status}")
                    )
                }
            }
    }

    private fun <T> fire(context:CoroutineContext,block:suspend()->Result<T>)=
        liveData<Result<T>>(context) {
            val result = try {
                block()
            }catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place:Place)= PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}