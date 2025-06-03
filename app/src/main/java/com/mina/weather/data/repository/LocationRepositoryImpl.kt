package com.mina.weather.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.LocationRepository
import com.mina.weather.domain.utils.states.LocationResult
import android.location.LocationListener

class LocationRepositoryImpl(
    private val context: Context
) : LocationRepository {

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var locationListener: LocationListener? = null

    override fun getCurrentLocation(callback: (LocationResult) -> Unit) {
        if (!hasLocationPermission()) {
            callback(LocationResult.PermissionDenied)
            return
        }

        if (!isLocationEnabled()) {
            callback(LocationResult.GpsDisabled)
            return
        }

        requestCurrentLocation(callback)
    }

    private fun requestCurrentLocation(callback: (LocationResult) -> Unit) {
        try {
            locationListener?.let {
                locationManager.removeUpdates(it)
            }

            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationListener?.let {
                        locationManager.removeUpdates(it)
                    }
                    callback(LocationResult.Success(LatLng(location.latitude, location.longitude)))
                }

                override fun onProviderDisabled(provider: String) {
                    callback(LocationResult.GpsDisabled)
                }

                override fun onProviderEnabled(provider: String) {}
            }

            val providers = listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER
            ).filter { locationManager.isProviderEnabled(it) }

            for (provider in providers) {
                locationManager.requestLocationUpdates(
                    provider,
                    0L,
                    0f,
                    locationListener as LocationListener
                )
            }

            for (provider in providers) {
                val lastKnownLocation = locationManager.getLastKnownLocation(provider)
                if (lastKnownLocation != null) {
                    locationListener?.let {
                        locationManager.removeUpdates(it)
                    }
                    callback(LocationResult.Success(LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)))
                    return
                }
            }

        } catch (e: SecurityException) {
            callback(LocationResult.PermissionDenied)
        } catch (e: Exception) {
            callback(LocationResult.Error(e.message ?: "Unknown error"))
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
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }
}