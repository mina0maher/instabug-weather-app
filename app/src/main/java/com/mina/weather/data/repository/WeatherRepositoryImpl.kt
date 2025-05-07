package com.mina.weather.data.repository

import com.mina.weather.data.mappers.toDayForecast
import com.mina.weather.data.mappers.toTodayForecast
import com.mina.weather.data.remote.fetchWeatherData
import com.mina.weather.data.remote.parseApiResponse
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.Result


class WeatherRepositoryImpl : WeatherRepository {
    override fun getTodayForecast(latLng: LatLng): Result<TodayForecast> {
        return try {
            val json = fetchWeatherData(latLng.latitude, latLng.longitude)
            val apiResponse = parseApiResponse(json)
            Result.Success(apiResponse.toTodayForecast())
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override fun getIncomingDaysForecast(latLng: LatLng): Result<List<DayForecast>> {
        return try {
            val json = fetchWeatherData(latLng.latitude, latLng.longitude)
            val apiResponse = parseApiResponse(json)
            Result.Success(apiResponse.days.map { it.toDayForecast() })
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }
}
