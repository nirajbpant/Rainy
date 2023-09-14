package com.example.rainy.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionHandler @Inject constructor(
    private val context: Context
) {

    fun requestLocationPermission(activity: Activity, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
