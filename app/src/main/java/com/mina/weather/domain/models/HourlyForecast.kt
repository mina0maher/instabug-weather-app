package com.mina.weather.domain.models

data class HourlyForecast(
    val hour:String,
    val temperature:Int,
    val icon:String
)
