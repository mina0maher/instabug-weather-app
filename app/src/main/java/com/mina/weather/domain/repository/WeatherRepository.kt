package com.mina.weather.domain.repository

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast

interface WeatherRepository{
    fun getTodayForecast(latLng: LatLng): TodayForecast
    fun getIncomingDaysForecast(latLng: LatLng): List<DayForecast>
}