package com.mina.weather.domain.repository

import com.mina.weather.domain.utils.states.LocationResult

interface LocationRepository {
    fun getCurrentLocation(callback: (LocationResult) -> Unit)
}