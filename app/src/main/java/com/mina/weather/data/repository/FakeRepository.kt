package com.mina.weather.data.repository

import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.HourlyForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.repository.WeatherRepository
import com.mina.weather.domain.utils.Result
import java.lang.Thread.sleep


class FakeRepository : WeatherRepository {
    override fun getTodayForecast(latLng: LatLng): Result<TodayForecast> {
        sleep(1000)
        return Result.Success(TodayForecast(
            latLng= LatLng(6.2,6.5),
            today = "Monday",
            date = "18 JUNE 2025",
            icon = "clear",
            status = "Clear Sky",
            temperature = 25,
            rain = 18,
            windSpeed = 20,
            humidity = 51,
            hourlyForecast = listOf(
                HourlyForecast(hour = "10:00", temperature = 25, icon = "wind"),
                HourlyForecast(hour = "13:00", temperature = 28, icon = "clear"),
                HourlyForecast(hour = "16:00", temperature = 30, icon = "partly cloudy"),
                HourlyForecast(hour = "10:00", temperature = 25, icon = "rain"),
                HourlyForecast(hour = "13:00", temperature = 28, icon = "storm"),
                HourlyForecast(hour = "16:00", temperature = 30, icon = "wind")

            )
        )
        )
    }


    override fun getIncomingDaysForecast(latLng: LatLng): Result<List<DayForecast>> {
        sleep(1000)
        return Result.Success(listOf(
            DayForecast(
                day = "Tuesday",
                icon = "cloudy",
                status = "Cloudy",
                highTemp = 29,
                lowTemp = 20
            ),
            DayForecast(
                day = "Wednesday",
                icon = "rain",
                status = "Rainy",
                highTemp = 26,
                lowTemp = 19
            ),
            DayForecast(
                day = "Thursday",
                icon = "storm",
                status = "Storm",
                highTemp = 24,
                lowTemp = 17
            ),
            DayForecast(
                day = "Friday",
                icon = "sunny",
                status = "Sunny",
                highTemp = 31,
                lowTemp = 22
            ),
            DayForecast(
                day = "Saturday",
                icon = "clear",
                status = "Clear",
                highTemp = 30,
                lowTemp = 21
            ),
            DayForecast(
                day = "Sunday",
                icon = "cloudy",
                status = "Cloudy",
                highTemp = 27,
                lowTemp = 18
            ),
            DayForecast(
                day = "Monday",
                icon = "partly_cloudy",
                status = "Partly Cloudy",
                highTemp = 28,
                lowTemp = 19
            ),
            DayForecast(
                day = "Next Tuesday",
                icon = "windy",
                status = "Windy",
                highTemp = 25,
                lowTemp = 17
            ),
            DayForecast(
                day = "Next Wednesday",
                icon = "foggy",
                status = "Foggy",
                highTemp = 23,
                lowTemp = 16
            ),
            DayForecast(
                day = "Next Thursday",
                icon = "snow",
                status = "Snowy",
                highTemp = 10,
                lowTemp = 3
            ),
            DayForecast(
                day = "Next Friday",
                icon = "rain",
                status = "Rainy",
                highTemp = 22,
                lowTemp = 14
            ),
            DayForecast(
                day = "Next Saturday",
                icon = "thunderstorm",
                status = "Thunderstorm",
                highTemp = 20,
                lowTemp = 12
            ),
            DayForecast(
                day = "Next Sunday",
                icon = "partly_cloudy",
                status = "Partly Cloudy",
                highTemp = 26,
                lowTemp = 15
            ),
            DayForecast(
                day = "Next Monday",
                icon = "cloudy",
                status = "Cloudy",
                highTemp = 24,
                lowTemp = 13
            )
        ))
    }

}
