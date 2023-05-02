package com.odogwudev.example.weatherforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odogwudev.example.weatherforecast.core.api.settings.SettingsRepository
import com.odogwudev.example.weatherforecast.core.model.DefaultLocation
import com.odogwudev.example.weatherforecast.event.MainViewEvent
import com.odogwudev.example.weatherforecast.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    fun handleEvent(mainViewEvent: MainViewEvent) {
        when (mainViewEvent) {
            is MainViewEvent.CheckLocationSettings -> {
                setState { copy(isLocationSettingEnabled = mainViewEvent.isEnabled) }
            }
            is MainViewEvent.GrantPermission -> {
                setState { copy(isPermissionGranted = mainViewEvent.isGranted) }
            }
            is MainViewEvent.ReceiveLocation -> {
                val defaultLocation = DefaultLocation(
                    longitude = mainViewEvent.longitude,
                    latitude = mainViewEvent.latitude
                )
                viewModelScope.launch {
                    settingsRepository.setDefaultLocation(defaultLocation)
                }
                setState { copy(defaultLocation = defaultLocation) }
            }
        }
    }

    private fun setState(stateReducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}


