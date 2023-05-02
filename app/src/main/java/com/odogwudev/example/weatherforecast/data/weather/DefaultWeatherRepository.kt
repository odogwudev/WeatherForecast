package com.odogwudev.example.weatherforecast.data.weather

import com.odogwudev.example.weatherforecast.BuildConfig
import com.odogwudev.example.weatherforecast.R
import com.odogwudev.example.weatherforecast.core.api.WeatherRepository
import com.odogwudev.example.weatherforecast.core.model.DefaultLocation
import com.odogwudev.example.weatherforecast.core.model.ExcludedData
import com.odogwudev.example.weatherforecast.core.model.Languages
import com.odogwudev.example.weatherforecast.core.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val openWeatherService: OpenWeatherService,
) : WeatherRepository {
    override fun fetchWeatherData(
        defaultLocation: DefaultLocation,
        language: String,
        units: String
    ): Flow<ApiResult<Weather>> = flow {

        val excludedData = "${ExcludedData.MINUTELY.value},${ExcludedData.ALERTS.value}"

        val response = openWeatherService.getWeatherData(
            longitude = defaultLocation.longitude,
            latitude = defaultLocation.latitude,
            excludedInfo = excludedData,
            units = units,
            language = getLanguageValue(language),
            appid = BuildConfig.OPEN_WEATHER_API_KEY
        )

        if (response.isSuccessful && response.body() != null) {
            val weatherData = response.body()!!.toCoreModel(unit = units)
            emit(ApiResult.Success(data = weatherData))
        } else {
            val errorMessage = mapResponseCodeToErrorMessage(response.code())
            emit(ApiResult.Error(errorMessage))
        }
    }.catch { throwable ->
        val errorMessage = when (throwable) {
            is IOException -> R.string.error_connection
            else -> R.string.error_generic
        }
        emit(ApiResult.Error(errorMessage))
    }

    private fun mapResponseCodeToErrorMessage(code: Int): Int = when (code) {
        HTTP_UNAUTHORIZED -> R.string.error_unauthorized
        in 400..499 -> R.string.error_client
        in 500..600 -> R.string.error_server
        else -> R.string.error_generic
    }

    private fun getLanguageValue(language: String) =
        Languages.values().first { it.languageName == language }.languageValue

}