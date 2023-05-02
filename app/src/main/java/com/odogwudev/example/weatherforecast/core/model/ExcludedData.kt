package com.odogwudev.example.weatherforecast.core.model

enum class ExcludedData(val value: String) {
    CURRENT("current"),
    HOURLY("hourly"),
    DAILY("daily"),
    MINUTELY("minutely"),
    ALERTS("alerts"),
}