package com.hkbufyp.hrms.ui.screen.announcement.detail

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

class AnnounceDetailViewModel(
    private val announcementRepository: AnnouncementRepository,
    private val announcementId: Int
): ViewModel() {

    private val _uiState = MutableStateFlow(AnnounceDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        fetchData()
    }

    fun onEvent(event: AnnounceDetailEvent) {
        when (event) {
            AnnounceDetailEvent.Refresh -> fetchData(true)
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
            announcementRepository.getAnnouncement(announcementId).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        isRefreshing = false,
                        message = "Could not get the announcement"
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
                                announcement = response.data
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                isRefreshing = false,
                                message = "Could not get the announcement"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}