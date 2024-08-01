package com.hkbufyp.hrms.ui.screen.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnnounceViewModel(
    private val announcementRepository: AnnouncementRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AnnounceUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AnnounceEvent) {
        when (event) {
            AnnounceEvent.Enter -> fetchData()
            AnnounceEvent.Refresh -> fetchData(true)
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isRefreshing = isRefresh,
                isLoadingData = !isRefresh
            )
        }

        viewModelScope.launch {
            announcementRepository.getAnnouncements().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isRefreshing = false,
                        isLoadingData = false
                    )
                }
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isRefreshing = false,
                                isLoadingData = false,
                                announcements = response.data ?: emptyList()
                            )
                        }
                    }
                    else -> {
                        _uiState.update { state ->
                            state.copy(
                                isRefreshing = false,
                                isLoadingData = false
                            )
                        }
                    }
                }
            }
        }
    }
}