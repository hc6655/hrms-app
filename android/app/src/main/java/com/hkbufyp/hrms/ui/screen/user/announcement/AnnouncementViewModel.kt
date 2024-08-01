package com.hkbufyp.hrms.ui.screen.user.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val announcementRepository: AnnouncementRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AnnouncementUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        fetchData()
    }

    fun onEvent(event: AnnouncementEvent) {
        when (event) {
            AnnouncementEvent.Refresh -> fetchData(true)
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isLoadingData = !isRefresh,
                isRefreshing = isRefresh
            )
        }

        viewModelScope.launch {
            announcementRepository.getAnnouncements().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
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
                                isLoadingData = false,
                                isRefreshing = false,
                                announcements = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
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
}