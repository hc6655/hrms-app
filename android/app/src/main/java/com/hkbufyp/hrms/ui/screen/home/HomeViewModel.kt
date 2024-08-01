package com.hkbufyp.hrms.ui.screen.home

import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedViewModel: SharedViewModel,
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val biometricRepository: BiometricRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private fun fetchBiometricStatus() {
        viewModelScope.launch {
            while (_uiState.value.isLoadingData) { delay(500) }
            biometricRepository.getBiometricStatus().collect {
                _uiState.update { state ->
                    state.copy(
                        shouldShowBiometricAlert = !it.isBiometricTokenExist &&
                                                    it.isBiometricAvailable &&
                                                    !state.showedBiometricAlert,
                        cryptoObject = biometricRepository.getCryptoObject(true)
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Enter -> {
                fetchData()
                fetchBiometricStatus()
            }
            is HomeEvent.ShowBiometricPrompt -> {
                _uiState.update { state ->
                    state.copy(
                        showBiometricPrompt = event.isShow,
                        showedBiometricAlert = true,
                        shouldShowBiometricAlert = false
                    )
                }
            }
            is HomeEvent.BiometricSucceeded -> {
                val cryptoObject = event.result.cryptoObject
                getToken(cryptoObject!!)
            }
            is HomeEvent.BiometricError -> {
                when (event.errorCode) {
                    7 -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun getToken(cryptoObject: CryptoObject) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoadingToken = true)
            }

            sharedViewModel.sharedUiState.value.employeeInfo?.employeeId?.let {
                biometricRepository.getEncryptedToken(
                    it, cryptoObject)
            }

            _uiState.update { state ->
                state.copy(isLoadingToken = false)
            }
        }
    }

    private fun fetchData() {
        fetchAnnouncements()
        updateUserPermission()
    }

    private fun updateUserPermission() {
        viewModelScope.launch {
            sharedViewModel.updateUserPermission()
            _uiState.update {
                it.copy(isLoadingData = false)
            }
        }
    }

    private fun fetchAnnouncements() {
        _uiState.update {
            it.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            announcementRepository.getAnnouncements(limit = 4).catch { exception ->

            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(announcements = response.data ?: emptyList())
                        }
                    }
                    is NetworkResponse.Failure -> {

                    }
                }
            }
        }
    }
}