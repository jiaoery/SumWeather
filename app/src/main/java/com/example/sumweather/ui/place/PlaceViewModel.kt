package com.example.sumweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sumweather.logic.Repository
import com.example.sumweather.logic.dao.PlaceDao
import com.example.sumweather.logic.model.Place

class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val  placeList = ArrayList<Place>()

    val palceLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlace(query)
    }

    fun searchPlace(place: String) {
        searchLiveData.value = place
    }

    fun savePlace(place:Place)= Repository.savePlace(place)

    fun getSavePlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}