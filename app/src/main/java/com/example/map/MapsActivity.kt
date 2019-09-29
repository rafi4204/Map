package com.example.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.FirebaseDatabase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

var retroCall:RetrofitCall?=null
    private var SYDNEY: LatLng? = null
    private var DESTINATION: LatLng? = null
    val ZOOM_LEVEL = 13f
    lateinit var mLastLocation: Location
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLocationPermissionGranted = false
    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        retroCall= RetrofitCall()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()


    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )

        }
        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient.lastLocation

                locationResult?.addOnSuccessListener { location: Location? ->
                    Log.d("2", location?.latitude.toString())
                    Log.d("2", location?.longitude.toString())


                    if (location != null) {
                        SYDNEY = LatLng(location.latitude, location.longitude)
                    }


                    val mapFragment: SupportMapFragment? =
                        supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
                    mapFragment?.getMapAsync(this)
                }


            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    mLocationPermissionGranted = true
                    getDeviceLocation()
                } else {
                    Toast.makeText(this, "Permission Denied!!Are you ", Toast.LENGTH_SHORT).show()
                }
                return
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
            addMarker(SYDNEY?.let { MarkerOptions().position(it) })
        }

        googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            Log.d("2", it.latitude.toString())
            Log.d("2", it.longitude.toString())
            DESTINATION = LatLng(it.latitude, it.longitude)
            googleMap.clear()
            googleMap.addPolyline(
                PolylineOptions()
                    .add(SYDNEY, DESTINATION)
                    .width(5F)
                    .color(R.color.colorPrimaryDark)
            )
            val clatlon=SYDNEY?.latitude.toString()+","+SYDNEY?.longitude.toString()
            val dlatlon=DESTINATION?.latitude.toString()+","+DESTINATION?.longitude.toString()

          retroCall?.getRetroObject(clatlon,dlatlon, getString(R.string.google_maps_key))


        })

    }
}
