package com.mina.weather.data.remote

import com.mina.weather.data.utils.ApiConstants
import java.net.HttpURLConnection
import java.net.URL

class WeatherApiService {

    fun fetchWeatherData(lat: Double, lng: Double): String {
        val urlString = "${ApiConstants.BASE_URL}$lat,$lng?unitGroup=${ApiConstants.UNIT_GROUP}&key=${ApiConstants.API_KEY}&contentType=${ApiConstants.CONTENT_TYPE}"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("HTTP error code: $responseCode")
            }

            val reader = connection.inputStream.bufferedReader()
            val response = StringBuilder()
            reader.useLines { lines ->
                lines.forEach { response.append(it) }
            }
            response.toString()
        } finally {
            connection.disconnect()
        }
    }
}
