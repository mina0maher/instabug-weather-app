package com.mina.weather.presentation.ui.utils.location

import android.content.IntentSender
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.mina.weather.presentation.ui.utils.helpers.Key.LOCATION_SETTINGS_INTERVAL_MILLIS
import com.mina.weather.presentation.ui.utils.helpers.Key.LOCATION_SETTINGS_REQUEST_CODE

fun Fragment.checkLocationSettings(
    onGpsEnabled: () -> Unit,
    onGpsRejected: () -> Unit
) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_SETTINGS_INTERVAL_MILLIS).build()

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)

    val settingsClient = LocationServices.getSettingsClient(requireContext())
    val task = settingsClient.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
        onGpsEnabled()
    }

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(requireActivity(), LOCATION_SETTINGS_REQUEST_CODE)
            } catch (sendEx: IntentSender.SendIntentException) {
                sendEx.printStackTrace()
                onGpsRejected()
            }
        } else {
            onGpsRejected()
        }
    }
}

