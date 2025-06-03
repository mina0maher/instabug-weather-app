package com.mina.weather.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.HourlyForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast

class WeatherLocalDataSourceImpl(
    private val dbHelper: WeatherDatabaseHelper
) : WeatherLocalDataSource {

    override fun saveTodayForecast(todayForecast: TodayForecast) {
        val db = dbHelper.writableDatabase
        db.delete("today_forecast", null, null)
        val values = ContentValues().apply {
            put("date", todayForecast.date)
            put("today", todayForecast.today)
            put("lat", todayForecast.latLng.latitude)
            put("lng", todayForecast.latLng.longitude)
            put("icon", todayForecast.icon)
            put("status", todayForecast.status)
            put("temperature", todayForecast.temperature)
            put("rain", todayForecast.rain)
            put("wind_speed", todayForecast.windSpeed)
            put("humidity", todayForecast.humidity)
        }
        db.insertWithOnConflict("today_forecast", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        saveHourlyForecast(todayForecast.hourlyForecast, todayForecast.date)
    }

    override fun getTodayForecast(): TodayForecast? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("today_forecast", null, null, null, null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                TodayForecast(
                    latLng = LatLng(
                        it.getDouble(it.getColumnIndexOrThrow("lat")),
                        it.getDouble(it.getColumnIndexOrThrow("lng"))
                    ),
                    today = it.getString(it.getColumnIndexOrThrow("today")),
                    date = it.getString(it.getColumnIndexOrThrow("date")),
                    icon = it.getString(it.getColumnIndexOrThrow("icon")),
                    status = it.getString(it.getColumnIndexOrThrow("status")),
                    temperature = it.getInt(it.getColumnIndexOrThrow("temperature")),
                    rain = it.getInt(it.getColumnIndexOrThrow("rain")),
                    windSpeed = it.getInt(it.getColumnIndexOrThrow("wind_speed")),
                    humidity = it.getInt(it.getColumnIndexOrThrow("humidity")),
                    hourlyForecast = getHourlyForecast(it.getString(it.getColumnIndexOrThrow("date")))
                )
            } else null
        }
    }

    override fun saveIncomingDays(days: List<DayForecast>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("incoming_days", null, null)
            for (day in days) {
                val values = ContentValues().apply {
                    put("day", day.day)
                    put("icon", day.icon)
                    put("status", day.status)
                    put("high_temp", day.highTemp)
                    put("low_temp", day.lowTemp)
                }
                db.insert("incoming_days", null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun getIncomingDays(): List<DayForecast> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("incoming_days", null, null, null, null, null, null)
        val days = mutableListOf<DayForecast>()
        cursor.use {
            while (it.moveToNext()) {
                days.add(
                    DayForecast(
                        day = it.getString(it.getColumnIndexOrThrow("day")),
                        icon = it.getString(it.getColumnIndexOrThrow("icon")),
                        status = it.getString(it.getColumnIndexOrThrow("status")),
                        highTemp = it.getInt(it.getColumnIndexOrThrow("high_temp")),
                        lowTemp = it.getInt(it.getColumnIndexOrThrow("low_temp"))
                    )
                )
            }
        }
        return days
    }

    override fun saveHourlyForecast(hourlyList: List<HourlyForecast>, date: String) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("hourly_forecast", "date = ?", arrayOf(date))
            for (hourly in hourlyList) {
                val values = ContentValues().apply {
                    put("date", date)
                    put("hour", hourly.hour)
                    put("temperature", hourly.temperature)
                    put("icon", hourly.icon)
                }
                db.insert("hourly_forecast", null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun getHourlyForecast(date: String): List<HourlyForecast> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("hourly_forecast", null, "date = ?", arrayOf(date), null, null, null)
        val hourlyList = mutableListOf<HourlyForecast>()
        cursor.use {
            while (it.moveToNext()) {
                hourlyList.add(
                    HourlyForecast(
                        hour = it.getString(it.getColumnIndexOrThrow("hour")),
                        temperature = it.getInt(it.getColumnIndexOrThrow("temperature")),
                        icon = it.getString(it.getColumnIndexOrThrow("icon"))
                    )
                )
            }
        }
        return hourlyList
    }
}

