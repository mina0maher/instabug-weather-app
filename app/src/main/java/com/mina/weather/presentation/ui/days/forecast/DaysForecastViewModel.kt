package com.mina.weather.presentation.ui.days.forecast

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.usecase.GetIncomingDaysForecastUseCase
import com.mina.weather.domain.utils.states.Result
import com.mina.weather.presentation.ui.utils.states.DaysUIState

class DaysForecastViewModel(
    private val getIncomingDaysForecastUseCase: GetIncomingDaysForecastUseCase
) : ViewModel() {

    private val _daysForecastState = MutableLiveData<DaysUIState<List<DayForecast>>>()
    val daysForecastState :LiveData<DaysUIState<List<DayForecast>>> = _daysForecastState

    fun loadDaysForecast(currentLocation:LatLng,forceRefresh: Boolean = false){
        val currentState = _daysForecastState.value
        if (!forceRefresh && currentState is DaysUIState.Success) {
            return
        }
        _daysForecastState.value = DaysUIState.Loading
        Thread{
            val result = getIncomingDaysForecastUseCase.execute(currentLocation)
            Handler(Looper.getMainLooper()).post {
                when (result) {
                    is Result.Error -> _daysForecastState.value = DaysUIState.Error(result.message)
                    is Result.Success -> _daysForecastState.value = DaysUIState.Success(result.data)
                }
            }
        }.start()
    }
}
