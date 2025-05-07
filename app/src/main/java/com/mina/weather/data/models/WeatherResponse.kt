package com.mina.weather.data.models

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val days: List<DayDto>,
    val currentConditions: CurrentConditionsDto
)