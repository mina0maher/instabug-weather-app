package com.mina.weather.data.local

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.HourlyForecast
import com.mina.weather.domain.models.TodayForecast

interface WeatherLocalDataSource {
    fun saveTodayForecast(todayForecast: TodayForecast)
    fun getTodayForecast(): TodayForecast?
    fun saveIncomingDays(days: List<DayForecast>)
    fun getIncomingDays(): List<DayForecast>
    fun saveHourlyForecast(hourlyList: List<HourlyForecast>, date: String)
    fun getHourlyForecast(date: String): List<HourlyForecast>
}