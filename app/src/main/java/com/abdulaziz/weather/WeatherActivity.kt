package com.abdulaziz.weather

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.abdulaziz.weather.databinding.ActivityWeatherInfoBinding
import com.abdulaziz.weather.models.Cordination
import com.github.ahmadnemati.wind.enums.TrendType
import java.text.SimpleDateFormat
import java.util.*


class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherInfoBinding
    private lateinit var myViewModel: MyViewModel
    private var currentTime = "00:00"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val cordination = intent.getSerializableExtra("latlang") as Cordination
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.setLocation(cordination.latitude, cordination.longitude)
        myViewModel.getWeather().observe(this) { weather ->
            getTime(weather.timezone)
            binding.apply {
                if (weather.name.length > 2)
                    supportActionBar?.title = weather.name
                else {
                    val gcd = Geocoder(this@WeatherActivity, Locale.getDefault())
                    val addresses: List<Address> =
                        gcd.getFromLocation(cordination.latitude, cordination.longitude, 1)
                    supportActionBar?.title = addresses[0].adminArea
                }
                cloudsCountTv.text = weather.clouds.all.toString()
                countryTv.text = weather.sys.country
                baseTv.text = weather.base

                timeTv.text = currentTime
                aboutTv.text = weather.weather[0].description
                val temp = weather.main.temp.plus(-273.15).toInt().toString()
                tempTv.text = "${temp}°"
                when (weather.weather[0].description) {
                    "clear sky" -> weatherIv.setImageResource(R.drawable.ic_sun)
                    "broken clouds" -> weatherIv.setImageResource(R.drawable.ic_cloudy)
                    "overcast clouds" -> weatherIv.setImageResource(R.drawable.ic_cloudy)
                    "scattered clouds" -> weatherIv.setImageResource(R.drawable.ic_cloudy)
                    "few clouds" -> weatherIv.setImageResource(R.drawable.ic_cloudy_day)
                    "mist" -> weatherIv.setImageResource(R.drawable.ic_mist)
                    "light rain" -> weatherIv.setImageResource(R.drawable.ic_rainy)
                    else -> weatherIv.setImageResource(R.drawable.ic_cloudy_day)
                }
                if (tempTv.text.isNotEmpty()) {
                    binding.animLayout.visibility = View.GONE
                }

                val pressure = weather.main.pressure * 0.02953
                windView.pressure = pressure.toFloat()
                windView.pressureUnit = "in Hg"
                windView.setWindSpeed(weather.wind.speed.toFloat())
                windView.windSpeedUnit = " m/s"
                windView.trendType = TrendType.UP
                windView.start()
                windView.animateBaroMeter()
            }
        }


    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(timezone: Int) {
        val default = TimeZone.getDefault().rawOffset / 1000
        val time = default - timezone
        val date = Date()
        date.hours = date.hours - time / 3600
        if (date.hours < 18) {
            binding.backIv.setImageResource(R.drawable.day)
        }
        currentTime = SimpleDateFormat("kk:mm").format(date)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}