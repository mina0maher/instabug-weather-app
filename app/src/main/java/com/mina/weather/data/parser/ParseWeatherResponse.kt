package com.mina.weather.data.parser

import com.mina.weather.data.models.CurrentConditionsDto
import com.mina.weather.data.models.DayDto
import com.mina.weather.data.models.HourDto
import com.mina.weather.data.models.WeatherResponse
import org.json.JSONObject

fun parseApiResponse(json: String): WeatherResponse {
    val jsonObject = JSONObject(json)

    val latitude = jsonObject.getDouble("latitude")
    val longitude = jsonObject.getDouble("longitude")

    val currentConditionsObj = jsonObject.getJSONObject("currentConditions")
    val currentConditions = CurrentConditionsDto(
        datetime = currentConditionsObj.getString("datetime"),
        temp = currentConditionsObj.getDouble("temp"),
        conditions = currentConditionsObj.getString("conditions"),
        icon = currentConditionsObj.getString("icon"),
        precipprob = currentConditionsObj.getDouble("precipprob"),
        windspeed = currentConditionsObj.getDouble("windspeed"),
        humidity = currentConditionsObj.getDouble("humidity")
    )

    val daysArray = jsonObject.getJSONArray("days")
    val days = mutableListOf<DayDto>()
    for (i in 0 until daysArray.length()) {
        val dayObj = daysArray.getJSONObject(i)
        val hoursArray = dayObj.getJSONArray("hours")
        val hours = mutableListOf<HourDto>()
        for (j in 0 until hoursArray.length()) {
            val hourObj = hoursArray.getJSONObject(j)
            hours.add(
                HourDto(
                    datetime = hourObj.getString("datetime"),
                    temp = hourObj.getDouble("temp"),
                    icon = hourObj.getString("icon")
                )
            )
        }

        days.add(
            DayDto(
                datetime = dayObj.getString("datetime"),
                tempmax = dayObj.getDouble("tempmax"),
                tempmin = dayObj.getDouble("tempmin"),
                temp = dayObj.getDouble("temp"),
                conditions = dayObj.getString("conditions"),
                icon = dayObj.getString("icon"),
                hours = hours
            )
        )
    }

    return WeatherResponse(
        latitude = latitude,
        longitude = longitude,
        days = days,
        currentConditions = currentConditions
    )
}
