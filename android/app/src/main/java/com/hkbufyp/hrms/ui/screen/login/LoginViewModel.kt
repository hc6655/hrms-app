package com.hkbufyp.hrms.ui.screen.login

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.hkbufyp.hrms.data.remote.dto.fcm.SetFCMTokenPayload
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.FCMRepository
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.hash
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class LoginViewModel(
    private val userRepository: UserRepository,
    private val fcmRepository: FCMRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val biometricRepository: BiometricRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isFCMTokenUploaded: StateFlow<Boolean> = userPreferencesRepository.isNewFCMTokenUploaded()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        initBiometric()
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> {
                login()
            }
            is LoginEvent.ShowBiometricPrompt -> {
                _uiState.update { state ->
                    state.copy(
                        showBiometricPrompt = event.isShow
                    )
                }
                if (event.isShow && !_uiState.value.isLoggingInWithBiometric) {
                    _uiState.update { state ->
                        state.copy(isLoggingInWithBiometric = true)
                    }
                } else if (!event.isShow && _uiState.value.isLoggingInWithBiometric) {
                    _uiState.update { state ->
                        state.copy(isLoggingInWithBiometric = false)
                    }
                }
            }
            is LoginEvent.UsernameChanged -> {
                _uiState.update {
                    it.copy(userName = event.name)
                }
            }
            is LoginEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(password = event.password)
                }
            }
            is LoginEvent.LoginWithBiometric -> {
                loginWithBiometric(event.cryptoObject)
            }
            LoginEvent.DismissBiometricAlert -> {
                _uiState.update { state ->
                    state.copy(shouldShowBiometricAlert = false)
                }
            }
        }
    }

    private fun initBiometric() {
        viewModelScope.launch {
            biometricRepository.getBiometricStatus().collect {
                _uiState.update { state ->
                    state.copy(
                        biometricStatus = it,
                        shouldShowBiometricAlert = it.isKeyInvalidated() && it.isBiometricAvailable
                    )
                }
            }

            if (_uiState.value.biometricStatus?.canAuthByBiometric() == true) {
                _uiState.update { state ->
                    state.copy(
                        cryptoObject = biometricRepository.getCryptoObject(false)
                    )
                }
            }
        }
    }

    private fun loginWithBiometric(cryptoObject: CryptoObject) {
        viewModelScope.launch {
            biometricRepository.decryptToken(cryptoObject).collect {
                if (it.isEmptyOrBlank()) {
                    _uiState.update { state ->
                        state.copy(
                            message = "Please use password to login.",
                            isLoggingInWithBiometric = false
                        )
                    }

                    _isShowSnackbar.emit(true)

                } else {
                    userRepository.loginWithBiometric(_uiState.value.userName, it).catch { exception ->
                        _uiState.update { state ->
                            state.copy(
                                message = exception.message ?: "",
                                isLoggingInWithBiometric = false
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }.collect { response ->
                        when (response) {
                            is NetworkResponse.Success -> {
                                println(response.data)
                                _uiState.update { state ->
                                    state.copy(
                                        userJwt = response.data
                                    )
                                }

                                if (!_isFCMTokenUploaded.value) {
                                    uploadFCMToken().catch {
                                        _uiState.update { state ->
                                            state.copy(
                                                isLoggingInWithBiometric = false,
                                                message = it.message ?: "Unexpected error"
                                            )
                                        }
                                    }.collect {
                                        _isLoggedIn.emit(true)
                                    }
                                } else {
                                    _isLoggedIn.emit(true)
                                }
                            }
                            is NetworkResponse.Failure -> {
                                _uiState.update { state ->
                                    state.copy(
                                        isLoggingInWithBiometric = false,
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
    }

    private fun login() {
        _uiState.update {
            it.copy(isLoggingIn = true)
        }

        viewModelScope.launch {
            userRepository.login(_uiState.value.userName, _uiState.value.password.hash()).catch { res ->
                _uiState.update {
                    it.copy(
                        isLoggingIn = false,
                        message = res.message ?: ""
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect {
                when (it) {
                    is NetworkResponse.Success -> {
                        println(it.data)
                        _uiState.update { state ->
                            state.copy(userJwt = it.data)
                        }

                        if (!_isFCMTokenUploaded.value) {
                            uploadFCMToken().catch {
                                _uiState.update { state ->
                                    state.copy(
                                        isLoggingIn = false,
                                        message = it.message ?: "Unexpected error"
                                    )
                                }
                            }.collect {
                                _isLoggedIn.emit(true)
                            }
                        } else {
                            _isLoggedIn.emit(true)
                        }

                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoggingIn = false,
                                message = it.errMessage ?: ""
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private suspend fun uploadFCMToken() =
        flow {
            val token = FirebaseMessaging.getInstance().token.await()
            fcmRepository.setFCMToken(
                id = _uiState.value.userName,
                payload = SetFCMTokenPayload(token)
            ).catch { throw it }.collect {
                when (it) {
                    is NetworkResponse.Success -> {
                        userPreferencesRepository.setNewFCMTokenUploaded(true)
                        emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        throw Exception(it.errMessage ?: "")
                    }
                }
            }
        }
}