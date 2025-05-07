package com.mina.weather.data.repository

import com.mina.weather.data.local.WeatherLocalDataSource
import com.mina.weather.data.remote.WeatherRemoteDataSource
import com.mina.weather.data.utils.helpers.ConnectivityChecker
import com.mina.weather.data.utils.NoCachedDataException
import com.mina.weather.data.utils.NoDataAvailableException
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val connectivityChecker: ConnectivityChecker
) : WeatherRepository {

    override fun getTodayForecast(latLng: LatLng): TodayForecast {
        return try {
            if (connectivityChecker.isConnected()) {
                val remoteData = remoteDataSource.getTodayForecast(latLng)
                localDataSource.saveTodayForecast(remoteData)
                remoteData
            } else {
                localDataSource.getTodayForecast() ?: throw NoCachedDataException()
            }
        } catch (e: Exception) {
            localDataSource.getTodayForecast() ?: throw NoDataAvailableException()
        }
    }

    override fun getIncomingDaysForecast(latLng: LatLng): List<DayForecast> {
        return try {
            if (connectivityChecker.isConnected()) {
                val remoteData = remoteDataSource.getIncomingDaysForecast(latLng)
                localDataSource.saveIncomingDays(remoteData)
                remoteData
            } else {
                localDataSource.getIncomingDays().takeIf { it.isNotEmpty() }
                    ?: throw NoCachedDataException()
            }
        } catch (e: Exception) {
            localDataSource.getIncomingDays().takeIf { it.isNotEmpty() }
                ?: throw NoDataAvailableException()
        }
    }
}



