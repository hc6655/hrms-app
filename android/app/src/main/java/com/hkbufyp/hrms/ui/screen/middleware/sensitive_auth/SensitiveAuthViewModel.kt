package com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.hash
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SensitiveAuthViewModel(
    private val userRepository: UserRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(SensitiveAuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isAuthSucceed = MutableSharedFlow<Boolean>()
    val isAuthSucceed = _isAuthSucceed.asSharedFlow()

    fun onEvent(event: SensitiveAuthEvent) {
        when (event) {
            SensitiveAuthEvent.Submit -> auth()
            SensitiveAuthEvent.UsePassword -> {
                _uiState.update { state ->
                    state.copy(isUsePassword = true)
                }
            }
            is SensitiveAuthEvent.PasswordChanged -> {
                _uiState.update { state ->
                    state.copy(password = event.password)
                }
            }
        }
    }

    private fun checkAuth() {

    }

    private fun auth() {
        val password = _uiState.value.password
        if (password.isEmptyOrBlank()) {
            _uiState.update { state ->
                state.copy(message = "Please input the password")
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        viewModelScope.launch {
            userRepository.login(employeeId, password.hash()).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = "Unexpected exception"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false
                            )
                        }
                        _isAuthSucceed.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Password is not correct"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}