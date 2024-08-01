package com.hkbufyp.hrms.ui.screen.management.attendance.method

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.toUpdatePayload
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClockInMethodViewModel(
    private val attendRepository: AttendRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ClockInMethodUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        fetchData()
    }

    fun onEvent(event: ClockInMethodEvent) {
        when (event) {
            ClockInMethodEvent.Submit -> submit()
            is ClockInMethodEvent.SwitchWifi -> switchWifi(event.on)
            is ClockInMethodEvent.SwitchBLE -> switchBle(event.on)
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            attendRepository.getMethod().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        message = exception.message ?: ""
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                method = response.data
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                message = response.errMessage ?: ""
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun switchWifi(on: Boolean) {
        _uiState.update { state ->
            state.copy(
                method = state.method?.copy(wifiEnable = on)
            )
        }
    }

    private fun switchBle(on: Boolean) {
        _uiState.update { state ->
            state.copy(
                method = state.method?.copy(bleEnable = on)
            )
        }
    }

    private fun submit() {
        val method = _uiState.value.method ?: return

        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        viewModelScope.launch {
            attendRepository.updateMethod(method.toUpdatePayload()).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: ""
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Update succeeded"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: ""
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}