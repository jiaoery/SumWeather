package com.example.sumweather.logic

import androidx.lifecycle.liveData
import com.example.sumweather.logic.model.Place
import com.example.sumweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

object Repository {
    fun searchPlace(query: String) = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlace(query)
            if(placeResponse.status=="ok"){
                val place = placeResponse.places
               Result.success(place)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch(e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}