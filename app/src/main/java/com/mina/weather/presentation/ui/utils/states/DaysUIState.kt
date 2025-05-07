package com.mina.weather.presentation.ui.utils.states

sealed class DaysUIState<out T> {
    data class Success<T>(val data: T) : DaysUIState<T>()
    data class Error(val message: String) : DaysUIState<Nothing>()
    data object Loading : DaysUIState<Nothing>()
}
