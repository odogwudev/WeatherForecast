package com.odogwudev.example.weatherforecast.event

sealed class MainViewEvent {

    data class GrantPermission(val isGranted: Boolean) : MainViewEvent()

    data class CheckLocationSettings(val isEnabled: Boolean) : MainViewEvent()

    data class ReceiveLocation(val latitude: Double, val longitude: Double) : MainViewEvent()

}