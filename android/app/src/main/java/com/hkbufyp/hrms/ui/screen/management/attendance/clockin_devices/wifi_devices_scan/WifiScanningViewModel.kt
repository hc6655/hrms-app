package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan

import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.domain.model.toWifiDevice
import com.hkbufyp.hrms.domain.repository.WifiRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WifiScanningViewModel(
    private val wifiRepository: WifiRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(WifiScanningUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isAddDeviceSucceeded = MutableSharedFlow<Boolean>()
    val isAddDeviceSucceeded = _isAddDeviceSucceeded.asSharedFlow()

    init {
        scanWifi()
    }

    fun onEvent(event: WifiScanningEvent) {
        when (event) {
            WifiScanningEvent.Refresh -> scanWifi()
            WifiScanningEvent.ConfirmSelect -> confirmSelectDevice()
            WifiScanningEvent.CancelSelect -> selectDevice(null)
            is WifiScanningEvent.SelectDevice -> selectDevice(event.device)
        }
    }

    private fun scanWifi() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            wifiRepository.scanWifi().collect {
                _uiState.update { state ->
                    state.copy(
                        wifiList = it,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun selectDevice(result: ScanResult?) {
        if (result == null) {
            _uiState.update { state ->
                state.copy(
                    selectedWifi = null,
                    isShowConfirmationBox = false
                )
            }
        } else {
            val device = result.toWifiDevice()
            _uiState.update { state ->
                state.copy(
                    selectedWifi = device,
                    isShowConfirmationBox = true
                )
            }
        }
    }


    private fun confirmSelectDevice() {
        _uiState.update { state ->
            state.copy(isShowConfirmationBox = false)
        }

        val device = _uiState.value.selectedWifi
        if (device != null) {
            _uiState.update { state ->
                state.copy(isSubmitting = true)
            }

            viewModelScope.launch {
                wifiRepository.addWifiDevice(device).catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            message = exception.message ?: "",
                            isSubmitting = false
                        )
                    }
                    _isShowSnackbar.emit(true)
                }.collect { response ->
                    when (response) {
                        is NetworkResponse.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    message = "Add Wifi device succeeded",
                                    isSubmitting = false
                                )
                            }

                            _isShowSnackbar.emit(true)
                            _isAddDeviceSucceeded.emit(true)
                        }
                        is NetworkResponse.Failure -> {
                            _uiState.update { state ->
                                state.copy(
                                    message = response.errMessage ?: "",
                                    isSubmitting = false
                                )
                            }
                            _isShowSnackbar.emit(true)
                        }
                    }
                }
            }
        }
    }
}