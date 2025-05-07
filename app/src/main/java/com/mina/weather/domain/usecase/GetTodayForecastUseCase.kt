package com.mina.weather.domain.usecase

import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.extensions.getDayName
import com.mina.weather.domain.utils.extensions.toCelsius
import com.mina.weather.domain.utils.states.Result
import com.mina.weather.domain.utils.extensions.to12HourFormat

class GetTodayForecastUseCase (
    private val weatherRepository: WeatherRepository
){
    fun execute(latLng: LatLng): Result<TodayForecast> {
        return when (val result = weatherRepository.getTodayForecast(latLng)) {
            is Result.Error -> result
            is Result.Success -> {
                val convertedHourly = result.data.hourlyForecast.map {
                    it.copy(
                        temperature = it.temperature.toCelsius(),
                        hour = it.hour.to12HourFormat()
                    )
                }

                val convertedToday = result.data.copy(
                    today = result.data.today.getDayName(),
                    temperature = result.data.temperature.toCelsius(),
                    hourlyForecast = convertedHourly
                )

                Result.Success(convertedToday)
            }
        }
    }
}