package com.example.rainy

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.rainy.core.PermissionHandler
import com.example.rainy.presentation.search.SearchScreen
import com.example.rainy.presentation.search.SearchViewModel
import com.example.rainy.presentation.ui.theme.RainyTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.location.LocationRequest;
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(this)
    }

    private val locationPermissionRequestCode = 1001

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RainyTheme {
               SearchScreen(viewModel = viewModel)
            }
        }
        fusedLocationClient = FusedLocationProviderClient(this)
        permissionHandler.requestLocationPermission(this, locationPermissionRequestCode)
    }

    /* Todo Given more time the methods below this comment would be its own repository for location information
        removing all the unnecessary business logic in this part of the screen
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                // Request location updates
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null // Optional Looper
                )
            } else {
                // Location permission denied, handle it as needed (e.g., show a message to the user)

            }
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                // Handle the received location here
                val cityName = getCityNameFromLocation(location)
                viewModel.initializeLocationWeather(cityName)
            }
        }
    }

    private fun getCityNameFromLocation(location: Location): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var cityName = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (!addresses.isNullOrEmpty()) {
                cityName = addresses[0].locality ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return cityName
    }


}


