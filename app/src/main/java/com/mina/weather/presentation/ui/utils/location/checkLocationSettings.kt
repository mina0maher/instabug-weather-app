package com.mina.weather.presentation.ui.utils.location

import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun Fragment.checkLocationSettings(
    onGpsEnabled: () -> Unit,
    onGpsRejected: () -> Unit
) {
    val locationManager = requireContext().getSystemService(LocationManager::class.java)

    fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    if (isLocationEnabled()) {
        onGpsEnabled()
    } else {
        try {
            val locationSettingsRequest = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (isLocationEnabled()) {
                    onGpsEnabled()
                } else {
                    onGpsRejected()
                }
            }

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            locationSettingsRequest.launch(intent)

        } catch (e: Exception) {
            e.printStackTrace()
            onGpsRejected()
        }
    }
}
