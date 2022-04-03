package com.abdulaziz.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abdulaziz.weather.databinding.ActivityMapsBinding
import com.abdulaziz.weather.models.Cordination
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val default = LatLng(41.0, 69.0)

        map.setOnMapClickListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLng(default))
    }

    override fun onMapClick(position: LatLng) {
        binding.btn.visibility = View.VISIBLE
        map.clear()
        map.addMarker(MarkerOptions().position(position).draggable(true))

        binding.btn.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("latlang", Cordination(position.latitude, position.longitude))
             startActivity(intent)
        }
    }
}