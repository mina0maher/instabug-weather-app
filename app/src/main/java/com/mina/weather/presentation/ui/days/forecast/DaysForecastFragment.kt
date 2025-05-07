package com.mina.weather.presentation.ui.days.forecast

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mina.weather.R
import com.mina.weather.data.local.WeatherDatabaseHelper
import com.mina.weather.data.local.WeatherLocalDataSourceImpl
import com.mina.weather.data.remote.WeatherRemoteDataSourceImpl
import com.mina.weather.data.repository.WeatherRepositoryImpl
import com.mina.weather.data.utils.AndroidConnectivityChecker
import com.mina.weather.presentation.ui.utils.adapters.MainAdapter
import com.mina.weather.domain.models.DayForecast
import com.mina.weather.domain.models.LatLng
import com.mina.weather.domain.usecase.GetIncomingDaysForecastUseCase
import com.mina.weather.presentation.ui.utils.states.DaysUIState
import com.mina.weather.presentation.ui.utils.helpers.Key.LATITUDE_KEY
import com.mina.weather.presentation.ui.utils.helpers.Key.LONGITUDE_KEY
import com.mina.weather.presentation.ui.utils.factories.ViewModelFactory
import com.mina.weather.presentation.ui.utils.helpers.getImageSource
import com.mina.weather.presentation.ui.utils.extensions.toDegree
import com.mina.weather.presentation.ui.utils.helpers.ToastHelper.showToast

class DaysForecastFragment : Fragment(R.layout.fragment_days_forecast) {
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var backImage:ImageView
    private lateinit var daysForecastRecyclerView: RecyclerView
    private lateinit var currentLocation:LatLng
    private lateinit var viewModel: DaysForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentLocation = getCurrentLocationFromArgs()

        initViewModel()

        initViews(view)

        observeState()

        setListeners()

        if(viewModel.daysForecastState.value !is DaysUIState.Success) {
            viewModel.loadDaysForecast(currentLocation)
        }

    }

    private fun getCurrentLocationFromArgs():LatLng{
        return LatLng(
            arguments?.getDouble(LATITUDE_KEY)?:0.0,
            arguments?.getDouble(LONGITUDE_KEY)?:0.0
        )
    }

    private fun initViewModel() {
        val weatherDatabaseHelper = WeatherDatabaseHelper(requireContext())
        val remoteDataSource = WeatherRemoteDataSourceImpl()
        val localDataSource = WeatherLocalDataSourceImpl(weatherDatabaseHelper)
        val connectivityChecker = AndroidConnectivityChecker(requireContext())

        val weatherRepository = WeatherRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            connectivityChecker = connectivityChecker
        )

        val getIncomingDaysForecastUseCase = GetIncomingDaysForecastUseCase(weatherRepository)

        val factory = ViewModelFactory {
            DaysForecastViewModel(getIncomingDaysForecastUseCase)
        }

        viewModel = ViewModelProvider(this, factory)[DaysForecastViewModel::class.java]
    }

    private fun initViews(view: View){
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        progressBar = view.findViewById(R.id.progress_loading)
        backImage = view.findViewById(R.id.image_back)
        daysForecastRecyclerView = view.findViewById(R.id.recycler_forecast_days)
    }

    private fun observeState() {
        viewModel.daysForecastState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DaysUIState.Loading -> isLoading(true)
                is DaysUIState.Success -> {
                    isLoading(false)
                    setupAdapter(result.data)
                }
                is DaysUIState.Error -> {
                    isLoading(false)
                    swipeRefreshLayout.isRefreshing = false
                    showToast(requireContext(),"Error: ${result.message}")
                }

            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
        daysForecastRecyclerView.visibility = if (isLoading) GONE else VISIBLE
        progressBar.visibility = if (isLoading) VISIBLE else GONE
    }

    private fun setupAdapter(daysForecast:List<DayForecast>) {

        val daysForecastAdapter = MainAdapter(
            items = daysForecast,
            layoutId = R.layout.item_day_forecast
        ) { forecast, itemView ->
            val dayText = itemView.findViewById<TextView>(R.id.text_day)
            val statusText = itemView.findViewById<TextView>(R.id.text_status)
            val highDegreeText = itemView.findViewById<TextView>(R.id.text_high_degree)
            val lowDegreeText = itemView.findViewById<TextView>(R.id.text_low_degree)
            val conditionImage = itemView.findViewById<ImageView>(R.id.image_condition)


            dayText.text = forecast.day
            statusText.text = forecast.status
            highDegreeText.text = forecast.highTemp.toDegree()
            lowDegreeText.text = forecast.lowTemp.toDegree()
            conditionImage.setImageResource(getImageSource(forecast.icon))
        }

        daysForecastRecyclerView.apply {
            adapter = daysForecastAdapter
        }
    }

    private fun setListeners(){

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadDaysForecast(currentLocation)
        }

        backImage.setOnClickListener {
            findNavController().popBackStack()
        }

    }


}