package com.mina.weather.data.models

data class DayDto(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val hours: List<HourDto>
)