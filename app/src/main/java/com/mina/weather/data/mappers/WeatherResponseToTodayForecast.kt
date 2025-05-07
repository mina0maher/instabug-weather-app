package com.mina.weather.data.mappers

import com.mina.weather.data.models.WeatherResponse
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast

fun WeatherResponse.toTodayForecast(): TodayForecast {
    val hourlyForecast = this.days.firstOrNull()?.hours?.map { it.toHourlyForecast() } ?: emptyList()
    return TodayForecast(
        latLng = LatLng(this.latitude, this.longitude),
        today = this.days.firstOrNull()?.datetime ?: "",
        date = this.days.firstOrNull()?.datetime ?: "",
        icon = this.currentConditions.icon,
        status = this.currentConditions.conditions,
        temperature = this.currentConditions.temp.toInt(),
        rain = this.currentConditions.precipprob.toInt(),
        windSpeed = this.currentConditions.windspeed.toInt(),
        humidity = this.currentConditions.humidity.toInt(),
        hourlyForecast = hourlyForecast
    )
}