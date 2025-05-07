package com.mina.weather.data.models

data class CurrentConditionsDto(
    val datetime: String,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val precipprob: Double,
    val windspeed: Double,
    val humidity: Double
)