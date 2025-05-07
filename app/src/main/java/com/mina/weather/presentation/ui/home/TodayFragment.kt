package com.mina.weather.presentation.ui.home


import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mina.weather.R
import com.mina.weather.data.repository.FakeRepository
import com.mina.weather.data.repository.LocationRepositoryImpl
import com.mina.weather.domain.models.HourlyForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.models.TodayForecast
import com.mina.weather.domain.usecase.GetCurrentLocationUseCase
import com.mina.weather.domain.usecase.GetTodayForecastUseCase
import com.mina.weather.presentation.ui.utils.helpers.Key.LATITUDE_KEY
import com.mina.weather.presentation.ui.utils.helpers.Key.LONGITUDE_KEY
import com.mina.weather.presentation.ui.utils.factories.ViewModelFactory
import com.mina.weather.presentation.ui.utils.adapters.MainAdapter
import com.mina.weather.presentation.ui.utils.helpers.ToastHelper.showToast
import com.mina.weather.presentation.ui.utils.states.TodayUIState
import com.mina.weather.presentation.ui.utils.location.checkLocationSettings
import com.mina.weather.presentation.ui.utils.helpers.getImageSource
import com.mina.weather.presentation.ui.utils.location.openAppSettings
import com.mina.weather.presentation.ui.utils.location.requestLocationPermission
import com.mina.weather.presentation.ui.utils.extensions.toDegree
import com.mina.weather.presentation.ui.utils.extensions.toKm
import com.mina.weather.presentation.ui.utils.extensions.toPercentage


class TodayFragment : Fragment(R.layout.fragment_today) {

    private lateinit var todayLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorText:TextView
    private lateinit var todayText:TextView
    private lateinit var dateText:TextView
    private lateinit var weatherConditionImage:ImageView
    private lateinit var weatherConditionText:TextView
    private lateinit var temperatureText:TextView
    private lateinit var rainText:TextView
    private lateinit var windSpeedText: TextView
    private lateinit var humidityText: TextView
    private lateinit var nextDaysText: TextView
    private lateinit var hourlyForecastRecycler: RecyclerView
    private lateinit var viewModel: TodayForecastViewModel
    private var currentLocation:LatLng = LatLng(0.0,0.0) //default values

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission(::onLocationPermissionResult)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        initViews(view)

        observeState()

        setListeners()

        if (viewModel.todayForecastState.value !is TodayUIState.Success) {
            viewModel.loadTodayForecast()
        }
    }

    private fun initViewModel(){
        val getTodayForecastUseCase = GetTodayForecastUseCase(FakeRepository())
        val getCurrentLocationUseCase = GetCurrentLocationUseCase(LocationRepositoryImpl(requireContext()))
        val factory = ViewModelFactory {
            TodayForecastViewModel(getTodayForecastUseCase, getCurrentLocationUseCase)
        }
        viewModel = ViewModelProvider(this, factory)[TodayForecastViewModel::class.java]
    }

    private fun initViews(view: View) {
        todayLayout = view.findViewById(R.id.layout_today)
        progressBar = view.findViewById(R.id.progress_loading)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        errorText = view.findViewById(R.id.text_error)
        todayText = view.findViewById(R.id.text_today)
        dateText = view.findViewById(R.id.text_date)
        weatherConditionImage = view.findViewById(R.id.image_weather_condition)
        weatherConditionText = view.findViewById(R.id.text_weather_condition)
        temperatureText = view.findViewById(R.id.text_temperature)
        rainText = view.findViewById(R.id.text_rain)
        windSpeedText = view.findViewById(R.id.text_wind_speed)
        humidityText = view.findViewById(R.id.text_humidity)
        nextDaysText = view.findViewById(R.id.text_next_days)
        hourlyForecastRecycler = view.findViewById(R.id.recycler_forecast_days)
    }

    private fun observeState() {
        viewModel.todayForecastState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TodayUIState.Loading -> isLoading(isLoading = true,isRefreshing = true)
                is TodayUIState.Success -> {
                    isLoading(isLoading = false, isRefreshing = false)
                    bindTodayData(result.data)
                }
                is TodayUIState.Error -> {
                    isLoading(isLoading = true,isRefreshing = false)
                    showErrorMessage(result.message)
                }
                is TodayUIState.PermissionDenied -> {
                    isLoading(isLoading = true,isRefreshing = false)
                    showErrorMessage("Location Permission Denied, Please enable location permission from settings")
                }
                is TodayUIState.GpsDisabled -> {
                    isLoading(isLoading = true,isRefreshing = false)
                    checkLocationSettings(
                        onGpsEnabled = {
                            viewModel.loadTodayForecast()
                        },
                        onGpsRejected = {
                            showErrorMessage("GPS still disabled")
                        }
                    )
                }
                is TodayUIState.LocationUnavailable -> {
                    isLoading(isLoading = true,isRefreshing = false)
                    showErrorMessage("Location unavailable")
                }
            }
        }
    }

    private fun bindTodayData(today: TodayForecast) {
        currentLocation = today.latLng
        todayText.text = today.today
        dateText.text = today.date
        weatherConditionImage.setImageResource(getImageSource(today.icon))
        weatherConditionText.text = today.status
        temperatureText.text = today.temperature.toDegree()
        rainText.text = today.rain.toPercentage()
        windSpeedText.text = today.windSpeed.toKm()
        humidityText.text = today.humidity.toPercentage()
        setupAdapter(today.hourlyForecast)
    }

    private fun setupAdapter(forecasts: List<HourlyForecast>) {
        val adapter = MainAdapter(
            items = forecasts,
            layoutId = R.layout.item_hourly_forecast
        ) { forecast, itemView ->
            val hourText = itemView.findViewById<TextView>(R.id.text_hour)
            val temperature = itemView.findViewById<TextView>(R.id.text_temperature)
            val weatherImage = itemView.findViewById<ImageView>(R.id.image_condition)

            hourText.text = forecast.hour
            temperature.text = forecast.temperature.toDegree()
            weatherImage.setImageResource(getImageSource(forecast.icon))
        }
        hourlyForecastRecycler.adapter = adapter
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTodayForecast()
        }

        nextDaysText.setOnClickListener {
            val bundle = Bundle().apply {
                putDouble(LATITUDE_KEY, currentLocation.latitude)
                putDouble(LONGITUDE_KEY,currentLocation.longitude)
            }
            findNavController().navigate(R.id.action_todayFragment_to_daysForecastFragment,bundle)
        }
    }

    private fun onLocationPermissionResult(isGranted:Boolean){
        if(isGranted){
            viewModel.loadTodayForecast()
        }else{
            showErrorMessage("Please enable location permission from settings")
            showToast(requireContext(), "Please enable location permission from settings")
            openAppSettings()
        }
    }

    private fun isLoading(isLoading: Boolean,isRefreshing:Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
        todayLayout.visibility = if (isLoading) GONE else VISIBLE
        progressBar.visibility = if (isLoading) VISIBLE else GONE
        errorText.visibility = if(isLoading) VISIBLE else GONE
        errorText.text = ""
    }

    private fun showErrorMessage(errorMessage:String){
        errorText.text = errorMessage
        errorText.visibility = VISIBLE
    }


}