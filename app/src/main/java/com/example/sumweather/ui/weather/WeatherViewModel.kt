package com.example.sumweather.ui.weather
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sumweather.logic.Repository
import com.example.sumweather.logic.model.Location

class WeatherViewModel(): ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng  = ""
    var locationLat = ""
    var placeName  = ""

    val weatherLivedData = Transformations.switchMap(locationLiveData){location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String,lat: String){
        locationLiveData.value = Location(lng,lat)
    }
}