package com.mina.weather.data.remote

import com.mina.weather.data.utils.ApiConstants
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL


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
            throw IOException("HTTP error: $responseCode ${connection.responseMessage}")
        }

        val reader = connection.inputStream.bufferedReader()
        val response = StringBuilder()
        reader.useLines { lines ->
            lines.forEach { response.append(it) }
        }
        response.toString()
    } catch (e: MalformedURLException) {
        throw IllegalArgumentException("Invalid URL format", e)
    } catch (e: SocketTimeoutException) {
        throw IOException("Connection timed out", e)
    } catch (e: IOException) {
        throw IOException("Network error: ${e.message}", e)
    } finally {
        connection.disconnect()
    }
}


