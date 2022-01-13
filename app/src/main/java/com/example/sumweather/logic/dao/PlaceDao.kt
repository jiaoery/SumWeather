package com.example.sumweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sumweather.SunnyWeatherApplication
import com.example.sumweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place: Place){
       sharedPrefences().edit {
           putString("place",Gson().toJson(place))
       }
    }

    fun getSavedPlace():Place{
        val placeJson = sharedPrefences().getString("place"," ")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isPlaceSaved() = sharedPrefences().contains("place")

    private fun sharedPrefences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",
        Context.MODE_PRIVATE)
}