package com.abdulaziz.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdulaziz.weather.models.Weather
import com.abdulaziz.weather.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel : ViewModel(){
    private var liveData = MutableLiveData<Weather>()
    private val API_KEY = "4c69c1fd04b314b176e3eae085556f59"

    private var latetude:Double = 0.0
    private var longitude:Double = 0.0

    fun setLocation(latetude:Double,longitude:Double){
        this.latetude = latetude
        this.longitude = longitude
    }

    fun getWeather() : LiveData<Weather> {
        ApiClient.apiService.getWeather(latetude, longitude, API_KEY).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful)
                    liveData.value = response.body()
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

            }


        })
        return liveData
    }

}