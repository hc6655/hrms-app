package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.ble.RemoveBleDevicePayload
import com.hkbufyp.hrms.domain.model.BleDevice
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.domain.repository.BleRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BleDeviceViewModel(
    private val bleRepository: BleRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(BleDeviceUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    fun onEvent(event: BleDeviceEvent) {
        when (event) {
            BleDeviceEvent.Enter -> fetchData()
            BleDeviceEvent.Refresh -> fetchData(true)
            BleDeviceEvent.Cancel -> selectRemove(null)
            BleDeviceEvent.Confirm -> removeDevice()
            is BleDeviceEvent.RemoveDevice -> selectRemove(event.device)
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh
            )
        }

        viewModelScope.launch {
            bleRepository.getBleDevices().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                devices = response.data
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                message = response.errMessage ?: "Unknown exception"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun selectRemove(device: BleDevice?) {
        _uiState.update { state ->
            state.copy(
                removeDevice = device,
                isShowConfirmationBox = device != null
            )
        }
    }

    private fun removeDevice() {
        val device = _uiState.value.removeDevice ?: return

        _uiState.update { state ->
            state.copy(
                isRemoving = true,
                isShowConfirmationBox = false
            )
        }

        viewModelScope.launch {
            bleRepository.removeBleDevice(
                RemoveBleDevicePayload(mac = device.mac)
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isRemoving = false,
                        message = exception.message ?: "",
                        removeDevice = null
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isRemoving = false,
                                message = "Remove device succeeded",
                                removeDevice = null
                            )
                        }

                        _isShowSnackbar.emit(true)
                        fetchData()
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isRemoving = false,
                                message = response.errMessage ?: "",
                                removeDevice = null
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}