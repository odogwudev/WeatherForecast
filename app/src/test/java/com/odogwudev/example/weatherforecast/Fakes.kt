package com.odogwudev.example.weatherforecast

import com.odogwudev.example.weatherforecast.core.model.CurrentWeather
import com.odogwudev.example.weatherforecast.core.model.Weather
import com.odogwudev.example.weatherforecast.data.weather.CurrentWeatherResponse
import com.odogwudev.example.weatherforecast.data.weather.WeatherResponse


val fakeSuccessWeatherResponse = WeatherResponse(
    current = CurrentWeatherResponse(
        temperature = 3.0f,
        feelsLike = 2.0f,
        weather = listOf()
    ),
    hourly = listOf(),
    daily = listOf()
)

val fakeSuccessMappedWeatherResponse = Weather(
    current = CurrentWeather(
        temperature = "3°C",
        feelsLike = "2°C",
        weather = listOf()
    ),
    hourly = listOf(),
    daily = listOf()
)