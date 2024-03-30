package com.saleem.admingecarparking.ui.fragments.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.ActivityMapsBinding
import com.saleem.admingecarparking.utils.LatLngUtil
import com.saleem.admingecarparking.utils.toast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation : Task<Location>
    private lateinit var latLng: LatLng
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        inItFusedLocation()
        obtainMapFragment()
        setOnClickListener()
    }

    private fun obtainMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setOnClickListener() {
        binding.btnAddLocation.setOnClickListener {
            returnTheLatLng()
        }
    }

    private fun inItFusedLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = fusedLocationClient.lastLocation
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 15)
        }
    }

    private fun returnTheLatLng(){
        if (::latLng.isInitialized) {
            val location = LatLngUtil.latLngToString(latLng)
            val resultIntent = Intent()
            resultIntent.putExtra("LOCATION", location)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            toast("Select a location first")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            addMarker(it)
        }

        lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Location").draggable(true))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,12f))
            }
        }

        if (permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED}){
            mMap.isMyLocationEnabled = true
        }
    }

    private fun addMarker(latLng: LatLng) {
        mMap.apply {
            clear()
            addMarker(MarkerOptions().position(latLng).title("Selected Location").draggable(true))
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
        this.latLng = latLng
    }
}