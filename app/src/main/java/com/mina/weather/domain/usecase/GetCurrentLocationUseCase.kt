package com.mina.weather.domain.usecase

import com.mina.weather.domain.repository.LocationRepository
import com.mina.weather.domain.utils.LocationResult

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    fun execute(callback: (LocationResult) -> Unit) {
        locationRepository.getCurrentLocation(callback)
    }
}
