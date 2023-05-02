package com.odogwudev.example.weatherforecast

import app.cash.turbine.test
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class WeatherRepositoryUnitTest {

    @MockK
    val mockOpenWeatherService = mockk<OpenWeatherService>(relaxed = true)
    @Test
    fun `when we fetch weather data successfully, then a successfully mapped result is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.success<WeatherResponse>(
            fakeSuccessWeatherResponse
        )

        val weatherRepository = createWeatherRepository()

        val expectedResult = fakeSuccessMappedWeatherResponse

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Success::class.java)
                Truth.assertThat((result as ApiResult.Success).data).isEqualTo(expectedResult)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and a server error occurs, then a server error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error<WeatherResponse>(
            500,
            "{}".toResponseBody()
        )

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_server)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and a client error occurs, then a client error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error<WeatherResponse>(
            404,
            "{}".toResponseBody()
        )

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_client)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and an unauthorized error occurs, then an unauthorized error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error<WeatherResponse>(
            401,
            "{}".toResponseBody()
        )

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_unauthorized)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and a generic error occurs, then a generic error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error<WeatherResponse>(
            800,
            "{}".toResponseBody()
        )

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_generic)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and an IOException is thrown, then a connection error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws IOException()

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_connection)
            }
            awaitComplete()
        }
    }

    @Test
    fun `when we fetch weather data and an unknown Exception is thrown, then a generic error is emitted`() = runBlocking {
        coEvery {
            mockOpenWeatherService.getWeatherData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception()

        val weatherRepository = createWeatherRepository()

        weatherRepository.fetchWeatherData(
            defaultLocation = DefaultLocation(
                longitude = 10.0,
                latitude = 12.90
            ),
            language = "English",
            units = "metric"
        ).test {
            awaitItem().also { result ->
                Truth.assertThat(result).isInstanceOf(ApiResult.Error::class.java)
                Truth.assertThat((result as ApiResult.Error).messageId).isEqualTo(R.string.error_generic)
            }
            awaitComplete()
        }
    }

    private fun createWeatherRepository(): WeatherRepository = DefaultWeatherRepository(
        openWeatherService = mockOpenWeatherService
    )
}