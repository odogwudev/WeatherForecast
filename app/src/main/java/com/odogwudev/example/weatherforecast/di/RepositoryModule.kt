package com.odogwudev.example.weatherforecast.di

import com.odogwudev.example.weatherforecast.core.api.WeatherRepository
import com.odogwudev.example.weatherforecast.core.api.settings.SettingsRepository
import com.odogwudev.example.weatherforecast.data.map.settings.DefaultSettingsRepository
import com.odogwudev.example.weatherforecast.data.weather.DefaultWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindWeatherRepository(weatherRepository: DefaultWeatherRepository): WeatherRepository

    @Binds
    fun bindSettingsRepository(settingsRepository: DefaultSettingsRepository): SettingsRepository

}