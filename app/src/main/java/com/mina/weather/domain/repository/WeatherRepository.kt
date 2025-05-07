package com.mina.weather.domain.repository

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.utils.states.Result

interface WeatherRepository{
    fun getTodayForecast(latLng: LatLng): Result<TodayForecast>
    fun getIncomingDaysForecast(latLng: LatLng): Result<List<DayForecast>>
}