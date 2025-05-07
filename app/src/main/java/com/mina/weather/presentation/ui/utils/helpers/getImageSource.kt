package com.mina.weather.presentation.ui.utils.helpers

import com.mina.weather.R

fun getImageSource(weatherDescription: String): Int {
    return when (weatherDescription.lowercase()) {
        "clear-day" -> R.drawable.sun

        "clear-night" , "partly-cloudy-night" -> R.drawable.clear_night

        "partly-cloudy-day" -> R.drawable.cloudy_sunny

        "cloudy" -> R.drawable.cloudy

        "rain", "showers-day", "showers-night" -> R.drawable.rainy

        "snow", "snow-showers-day", "snow-showers-night" -> R.drawable.snowy

        "thunder-rain", "thunder-showers-day", "thunder-showers-night" -> R.drawable.storm

        "fog", "wind" -> R.drawable.cloudy

        else -> R.drawable.cloudy_sunny
    }
}
