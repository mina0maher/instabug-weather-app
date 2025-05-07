package com.mina.weather.domain.usecase

import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.extensions.getDayName
import com.mina.weather.domain.utils.extensions.toCelsius
import com.mina.weather.domain.utils.extensions.to12HourFormat
import com.mina.weather.domain.utils.helpers.tryToExecute


class GetTodayForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(latLng: LatLng)= tryToExecute {
            val todayForecast = weatherRepository.getTodayForecast(latLng)
            val convertedHourly = todayForecast.hourlyForecast.map {
                it.copy(
                    temperature = it.temperature.toCelsius(),
                    hour = it.hour.to12HourFormat()
                )
            }

            todayForecast.copy(
                today = todayForecast.today.getDayName(),
                temperature = todayForecast.temperature.toCelsius(),
                hourlyForecast = convertedHourly
            )
    }
}