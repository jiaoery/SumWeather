package com.example.sumweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.sumweather.R
import com.example.sumweather.databinding.ActivityWeatherBinding
import com.example.sumweather.databinding.ForecastItemBinding
import com.example.sumweather.logic.Repository.refreshWeather
import com.example.sumweather.logic.model.Weather
import com.example.sumweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater

class WeatherActivity : AppCompatActivity() {
    lateinit var binding:ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng")?:""
        }

        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat")?:""
        }

        if(viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name")?:""
        }

        viewModel.weatherLivedData.observe(this){result->
            val weather  = result.getOrNull()
            if (weather !=null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.swipeRefresh.setColorSchemeColors(getColor(R.color.colorPrimary))
        }else{
            binding.swipeRefresh.setColorSchemeColors(R.color.colorPrimary)
        }
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.llNow.navBtn.setOnClickListener {
            binding.drawerLayout
        }
        binding.drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
    }


    fun showWeatherInfo(weather: Weather){
        binding.llNow.placeName.text = viewModel.placeName
        val realTime = weather.realtime
        val daily = weather.daily
        //填充now.xml中的数据
        val currentTempText = "${realTime.temperature.toInt()} ℃"
        binding.llNow.currentTemp.text= currentTempText
        binding.llNow.currentSky.text = getSky(realTime.skycon).info
        val currentPM25Text = "空气指数${realTime.airQuality.aqi.chn.toInt()}"
        binding.llNow.currentAQI.text = currentPM25Text
        binding.llNow.nowLayout.setBackgroundResource(getSky(realTime.skycon).bg)

        //填充forecast.xml布局中的数据
        binding.llForecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = ForecastItemBinding.inflate(layoutInflater);
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            view.dateInfo.text = simpleDateFormat.format(temperature)
            val sky = getSky(skycon.value)
            view.skyIcon.setImageResource(sky.icon)
            view.skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            view.temperatureInfo.text = tempText
            binding.llForecast.forecastLayout.addView(view.root)
        }

        //填充life_index.xml
        val lifeIndex = daily.lifeIndex
        binding.llLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.llLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.llLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.llLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }
}