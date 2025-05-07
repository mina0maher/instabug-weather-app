package com.mina.weather.data.remote

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast

interface WeatherRemoteDataSource {
     fun getTodayForecast(latLng: LatLng): TodayForecast
     fun getIncomingDaysForecast(latLng: LatLng): List<DayForecast>
}