package com.mina.weather.domain.utils.extensions

fun Int.toCelsius(): Int {
    return (this - 32) * 5 / 9
}