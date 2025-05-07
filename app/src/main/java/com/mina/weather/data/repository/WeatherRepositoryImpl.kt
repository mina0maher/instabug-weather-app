package com.mina.weather.data.repository

import com.mina.weather.data.mappers.toDayForecast
import com.mina.weather.data.mappers.toTodayForecast
import com.mina.weather.data.remote.fetchWeatherData
import com.mina.weather.data.remote.parseApiResponse
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.states.Result


class WeatherRepositoryImpl : WeatherRepository {
    override fun getTodayForecast(latLng: LatLng): TodayForecast {
        val json = fetchWeatherData(latLng.latitude, latLng.longitude)
        val apiResponse = parseApiResponse(json)
        return apiResponse.toTodayForecast()
    }

    override fun getIncomingDaysForecast(latLng: LatLng): List<DayForecast> {
        val json = fetchWeatherData(latLng.latitude, latLng.longitude)
        val apiResponse = parseApiResponse(json)
        return apiResponse.days.map { it.toDayForecast() }
    }
}

