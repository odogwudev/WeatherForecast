package com.odogwudev.example.weatherforecast.state

import com.odogwudev.example.weatherforecast.core.model.DefaultLocation

data class MainViewState(
    val isPermissionGranted: Boolean = false,
    val isLocationSettingEnabled: Boolean = false,
    val defaultLocation: DefaultLocation? = null
)
