package com.abdulaziz.weather.retrofit

import com.abdulaziz.weather.models.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double,
                 @Query("lon") longitude: Double,
                 @Query("appid") app_id: String): Call<Weather>

}