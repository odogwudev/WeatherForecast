package com.odogwudev.example.weatherforecast.core.api

import com.odogwudev.example.weatherforecast.core.model.DefaultLocation
import com.odogwudev.example.weatherforecast.core.model.Weather
import com.odogwudev.example.weatherforecast.data.weather.ApiResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeatherData(
        defaultLocation: DefaultLocation,
        language: String,
        units: String
    ): Flow<ApiResult<Weather>>
}