package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan

import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.ble.AddBleDevicePayload
import com.hkbufyp.hrms.domain.model.toBleDevice
import com.hkbufyp.hrms.domain.model.toWifiDevice
import com.hkbufyp.hrms.domain.repository.BleRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BleScanningViewModel(
    private val bleRepository: BleRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(BleScanningUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isAddDeviceSucceeded = MutableSharedFlow<Boolean>()
    val isAddDeviceSucceeded = _isAddDeviceSucceeded.asSharedFlow()

    fun onEvent(event: BleScanningEvent) {
        when (event) {
            BleScanningEvent.Enter -> scanBle()
            BleScanningEvent.Refresh -> scanBle()
            BleScanningEvent.Confirm -> addDevice()
            BleScanningEvent.Cancel -> selectDevice(null)
            is BleScanningEvent.SelectDevice -> selectDevice(event.v)
        }
    }

    private fun scanBle() {
        _uiState.update { state ->
            state.copy(
                isScanning = true,
                bleList = mutableStateMapOf()
            )
        }

        viewModelScope.launch {
            bleRepository.scanBLE().collect { result ->
                if (result != null) {
                    _uiState.value.bleList[result.device.address] = result
                } else {
                    _uiState.update { state ->
                        state.copy(isScanning = false)
                    }
                }
            }
        }
    }

    private fun selectDevice(result: ScanResult?) {
        if (result == null) {
            _uiState.update { state ->
                state.copy(
                    selectedBle = null,
                    isShowConfirmationBox = false
                )
            }
        } else {
            val device = result.toBleDevice()
            _uiState.update { state ->
                state.copy(
                    selectedBle = device,
                    isShowConfirmationBox = true
                )
            }
        }
    }

    private fun addDevice() {
        val device = _uiState.value.selectedBle ?: return

        _uiState.update { state ->
            state.copy(
                isSubmitting = true,
                isShowConfirmationBox = false
            )
        }

        viewModelScope.launch {
            bleRepository.addBleDevice(
                AddBleDevicePayload(
                    mac = device.mac,
                    name = device.name
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Add Ble device succeeded"
                            )
                        }

                        _isShowSnackbar.emit(true)
                        _isAddDeviceSucceeded.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: "Unknown exception"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}