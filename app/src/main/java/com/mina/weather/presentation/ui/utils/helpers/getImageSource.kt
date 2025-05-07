package com.mina.weather.presentation.ui.utils.helpers

import com.mina.weather.R

fun getImageSource(weatherDescription: String): Int {
    return when (weatherDescription.lowercase()) {
        "clear" -> R.drawable.sun
        "partly cloudy" -> R.drawable.cloudy_sunny
        "cloudy" -> R.drawable.cloudy
        "rain" -> R.drawable.rainy
        "snow" -> R.drawable.snowy
        "storm" -> R.drawable.storm
        else -> R.drawable.cloudy_sunny
    }
}