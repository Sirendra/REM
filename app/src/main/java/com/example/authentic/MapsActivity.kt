package com.example.authentic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{
    //declare the data usage and type
    private lateinit var mMap: GoogleMap
    private lateinit var latLng:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    //function to show map when the user press each list in our application
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val bundle = intent.extras
        //The lines below is to get the data from fragment into double and store in the "latLng"
        var point1Lat = bundle?.getString("point1lat").toString().toDouble()
        var point1Lon = bundle?.getString("point1lon").toString().toDouble()
        var name = bundle?.getString("selRoute").toString()
        latLng = LatLng(point1Lat,point1Lon)

        mMap.addMarker(MarkerOptions().position(latLng).title(name)) //show the map of that specific location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) //make it zoom to that location 15
        if (name==null) {//check if it's null then no show
            finish()
        }
    }
}