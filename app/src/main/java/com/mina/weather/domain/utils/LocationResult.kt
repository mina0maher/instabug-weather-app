package com.mina.weather.domain.utils

import com.mina.weather.domain.models.LatLng


sealed class LocationResult {
    data class Success(val latLng: LatLng) : LocationResult()
    data object PermissionDenied : LocationResult()
    data object GpsDisabled : LocationResult()
    data object LocationUnavailable : LocationResult()
    data class Error(val message: String) : LocationResult()
}
