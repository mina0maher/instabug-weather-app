package com.mina.weather.domain.usecase


import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.extensions.getDayName
import com.mina.weather.domain.utils.extensions.toCelsius
import com.mina.weather.domain.utils.helpers.tryToExecute

class GetIncomingDaysForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(latLng: LatLng) = tryToExecute {
        weatherRepository.getIncomingDaysForecast(latLng)
            .map { dayForecast ->
                dayForecast.copy(
                    day = dayForecast.day.getDayName(),
                    lowTemp = dayForecast.lowTemp.toCelsius(),
                    highTemp = dayForecast.highTemp.toCelsius()
                )
            }.slice(1..5)
    }
}