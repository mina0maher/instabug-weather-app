package com.mina.weather.data.remote

import com.mina.weather.data.mappers.toDayForecast
import com.mina.weather.data.mappers.toTodayForecast
import com.mina.weather.data.utils.ApiConstants
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {

    override fun getTodayForecast(latLng: LatLng): TodayForecast {
        val json = fetchWeatherData(latLng.latitude, latLng.longitude)
        val response = parseApiResponse(json)
        return response.toTodayForecast()
    }

    override fun getIncomingDaysForecast(latLng: LatLng): List<DayForecast> {
        val json = fetchWeatherData(latLng.latitude, latLng.longitude)
        val response = parseApiResponse(json)
        return response.days.map { day ->
            day.toDayForecast()
        }
    }

    private fun fetchWeatherData(lat: Double, lng: Double): String {
        val urlString = "${ApiConstants.BASE_URL}$lat,$lng?unitGroup=${ApiConstants.UNIT_GROUP}&key=${ApiConstants.API_KEY}&contentType=${ApiConstants.CONTENT_TYPE}"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw IOException("HTTP error code: $responseCode")
        }

        return connection.inputStream.bufferedReader().use { it.readText() }
    }

}