package com.mina.weather.presentation.ui.utils.states

sealed class TodayUIState<out T> {
    data class Success<T>(val data: T) : TodayUIState<T>()
    data class Error(val message: String) : TodayUIState<Nothing>()
    data object Loading : TodayUIState<Nothing>()
    data object PermissionDenied : TodayUIState<Nothing>()
    data object GpsDisabled : TodayUIState<Nothing>()
    data object LocationUnavailable : TodayUIState<Nothing>()
}
