package com.mina.weather.presentation.ui.home

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.usecase.GetCurrentLocationUseCase
import com.mina.weather.domain.usecase.GetTodayForecastUseCase
import com.mina.weather.domain.utils.states.LocationResult
import com.mina.weather.domain.utils.states.Result
import com.mina.weather.presentation.ui.utils.states.TodayUIState

class TodayForecastViewModel(
    private val getTodayForecastUseCase: GetTodayForecastUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _todayForecastState = MutableLiveData<TodayUIState<TodayForecast>>()
    val todayForecastState: LiveData<TodayUIState<TodayForecast>> = _todayForecastState

    fun loadTodayForecast(forceRefresh: Boolean = false) {
        val currentState = _todayForecastState.value
        if (!forceRefresh && currentState is TodayUIState.Success) {
            return
        }
        _todayForecastState.value = TodayUIState.Loading

        getCurrentLocationUseCase.execute { locationResult ->
            when (locationResult) {
                is LocationResult.Success -> {
                    Thread {
                        val result = getTodayForecastUseCase.execute(locationResult.latLng)
                        Handler(Looper.getMainLooper()).post {
                            when (result) {
                                is Result.Error -> _todayForecastState.value = TodayUIState.Error(result.message)
                                is Result.Success -> _todayForecastState.value = TodayUIState.Success(result.data)
                            }
                        }
                    }.start()
                }
                is LocationResult.PermissionDenied -> {
                    _todayForecastState.postValue(TodayUIState.PermissionDenied)
                }
                is LocationResult.GpsDisabled -> {
                    _todayForecastState.postValue(TodayUIState.GpsDisabled)
                }
                is LocationResult.LocationUnavailable -> {
                    _todayForecastState.postValue(TodayUIState.LocationUnavailable)
                }
                is LocationResult.Error -> {
                    _todayForecastState.postValue(TodayUIState.Error(locationResult.message))
                }
            }
        }
    }
}
