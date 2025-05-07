package com.mina.weather.data.mappers

import com.mina.weather.data.models.DayDto
import com.mina.weather.domain.models.DayForecast

fun DayDto.toDayForecast(): DayForecast {
    return DayForecast(
        day = this.datetime,
        icon = this.icon,
        status = this.conditions,
        highTemp = this.tempmax.toInt(),
        lowTemp = this.tempmin.toInt()
    )
}