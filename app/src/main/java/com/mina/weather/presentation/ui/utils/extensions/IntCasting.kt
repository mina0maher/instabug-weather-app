package com.mina.weather.presentation.ui.utils.extensions

fun Int.toDegree():String{
    return this.toString().plus("°C")
}
fun Int.toKm():String{
    return this.toString().plus(" km/h")
}
fun Int.toPercentage():String{
    return this.toString().plus(" %")
}