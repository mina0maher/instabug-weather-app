package com.mina.weather.domain.models

data class DayForecast(
    val day:String,
    val icon:String,
    val status:String,
    val highTemp:Int,
    val lowTemp:Int
)
