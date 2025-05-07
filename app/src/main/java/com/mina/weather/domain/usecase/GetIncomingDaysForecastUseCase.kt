package com.mina.weather.domain.usecase

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.extensions.getDayName
import com.mina.weather.domain.utils.extensions.toCelsius
import com.mina.weather.domain.utils.states.Result

class GetIncomingDaysForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(latLng: LatLng): Result<List<DayForecast>> {
        return when (val result = weatherRepository.getIncomingDaysForecast(latLng)) {
            is Result.Error -> result
            is Result.Success -> {
                val convertedList = result.data.map { dayForecast ->
                    dayForecast.copy(
                        day = dayForecast.day.getDayName(),
                        lowTemp = dayForecast.lowTemp.toCelsius(),
                        highTemp = dayForecast.highTemp.toCelsius()
                    )
                }.take(5)
                Result.Success(convertedList)
            }
        }
    }

}