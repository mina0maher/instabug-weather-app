package com.mina.weather.data.mappers

import com.mina.weather.data.models.HourDto
import com.mina.weather.domain.models.HourlyForecast

fun HourDto.toHourlyForecast(): HourlyForecast {
    return HourlyForecast(
        hour = this.datetime,
        temperature = this.temp.toInt(),
        icon = this.icon
    )
}