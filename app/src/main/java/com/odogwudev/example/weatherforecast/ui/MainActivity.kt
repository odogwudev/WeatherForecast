package com.odogwudev.example.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.odogwudev.example.weatherforecast.MainViewModel
import com.odogwudev.example.weatherforecast.event.MainViewEvent
import com.odogwudev.example.weatherforecast.state.MainViewState
import com.odogwudev.example.weatherforecast.ui.theme.WeatherForecastTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val locationRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mainViewModel.handleEvent(MainViewEvent.CheckLocationSettings(isEnabled = true))
            } else {
                mainViewModel.handleEvent(MainViewEvent.CheckLocationSettings(isEnabled = false))
            }
        }
    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            mainViewModel.handleEvent(MainViewEvent.GrantPermission(isGranted = isGranted))
        }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createLocationRequest(
            activity = this@MainActivity,
            locationRequestLauncher = locationRequestLauncher
        ) {
            mainViewModel.handleEvent(MainViewEvent.CheckLocationSettings(isEnabled = true))
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WeatherForecastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = mainViewModel.state.collectAsState().value

                    CheckForPermissions(
                        onPermissionGranted = {
                            mainViewModel.handleEvent(MainViewEvent.GrantPermission(isGranted = true))
                        },
                        onPermissionDenied = {
                            OnPermissionDenied(activityPermissionResult = permissionRequestLauncher)
                        }
                    )

                    InitMainScreen(state)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun InitMainScreen(state: MainViewState) {
        when {
            state.isLocationSettingEnabled && state.isPermissionGranted -> {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        mainViewModel.handleEvent(
                            MainViewEvent.ReceiveLocation(
                                longitude = location.longitude,
                                latitude = location.latitude
                            )
                        )
                    }
                WeatherAppScreensConfig(
                    navController = rememberNavController(),
                    homeViewModel = viewModel(),
                    settingsViewModel = viewModel()
                )
            }
            state.isLocationSettingEnabled && !state.isPermissionGranted -> {
                RequiresPermissionsScreen()
            }
            !state.isLocationSettingEnabled && !state.isPermissionGranted -> {
                EnableLocationSettingScreen()
            }
            else -> RequiresPermissionsScreen()
        }
    }
}