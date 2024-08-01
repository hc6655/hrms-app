package com.hkbufyp.hrms.ui.screen.management.log.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.LogRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginLogViewModel(
    private val logRepository: LogRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginLogUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            logRepository.getLoginLog().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                logs = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
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