package com.mina.weather.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.LocationRepository
import com.mina.weather.domain.utils.LocationResult

class LocationRepositoryImpl(private val context: Context) : LocationRepository {

    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation(callback: (LocationResult) -> Unit) {
        when {
            !hasLocationPermission() -> callback(LocationResult.PermissionDenied)
            !isLocationEnabled() -> callback(LocationResult.GpsDisabled)
            else -> requestLocation(callback)
        }
    }

    private fun requestLocation(callback: (LocationResult) -> Unit) {
        if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(LocationResult.PermissionDenied)
            return
        }
        fusedLocationProvider.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    callback(LocationResult.Success(LatLng(it.latitude, it.longitude)))
                } ?: callback(LocationResult.LocationUnavailable)
            }
            .addOnFailureListener {
                callback(LocationResult.Error(it.message ?: "Unknown error"))
            }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}