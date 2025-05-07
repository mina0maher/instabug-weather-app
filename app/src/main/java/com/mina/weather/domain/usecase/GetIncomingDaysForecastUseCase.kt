package com.mina.weather.domain.usecase

import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.WeatherRepository

class GetIncomingDaysForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(latLng: LatLng) = weatherRepository.getIncomingDaysForecast(latLng)
}