package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.domain.repository.WifiRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WifiDeviceViewModel(
    private val wifiRepository: WifiRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(WifiDeviceUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    fun onEvent(event: WifiDeviceEvent) {
        when (event) {
            WifiDeviceEvent.Enter -> fetchData()
            WifiDeviceEvent.Refresh -> fetchData(true)
            WifiDeviceEvent.CancelRemove -> selectRemove(null)
            WifiDeviceEvent.ConfirmRemove -> removeDevice()
            is WifiDeviceEvent.RemoveDevice -> selectRemove(event.device)
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
            wifiRepository.getWifiDevices().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        message = exception.message ?: ""
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
                                message = response.errMessage ?: ""
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun selectRemove(device: WifiDevice?) {
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
            wifiRepository.removeWifiDevice(device).catch { exception ->
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