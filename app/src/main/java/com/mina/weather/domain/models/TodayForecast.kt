package com.mina.weather.domain.models

data class TodayForecast (
    val latLng: LatLng,
    val today:String,
    val date:String,
    val icon:String,
    val status:String,
    val temperature:Int,
    val rain:Int,
    val windSpeed:Int,
    val humidity:Int,
    val hourlyForecast: List<HourlyForecast>
)