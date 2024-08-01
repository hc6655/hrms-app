package com.hkbufyp.hrms.ui.screen.management.announcement.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.Announcement
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageAnnouncementViewModel(
    private val announcementRepository: AnnouncementRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ManageAnnouncementUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    fun onEvent(event: ManageAnnouncementEvent) {
        when (event) {
            ManageAnnouncementEvent.Enter -> fetchData()
            ManageAnnouncementEvent.Refresh -> fetchData(true)
            ManageAnnouncementEvent.CancelRemove -> cancelRemove()
            ManageAnnouncementEvent.Remove -> remove()
            is ManageAnnouncementEvent.SelectRemove -> selectRemove(event.obj)
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

    private fun selectRemove(obj: Announcement) {
        _uiState.update { state ->
            state.copy(
                removingAnnounce = obj,
                isShowAlert = true
            )
        }
    }

    private fun cancelRemove() {
        _uiState.update { state ->
            state.copy(
                removingAnnounce = null,
                isShowAlert = false
            )
        }
    }

    private fun remove() {
        _uiState.update { state ->
            state.copy(
                isShowAlert = false,
                isRemoving = true
            )
        }

        val announcement = _uiState.value.removingAnnounce
        if (announcement == null) {
            _uiState.update { state ->
                state.copy(
                    isRemoving = false,
                    message = "Please select an announcement"
                )
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        viewModelScope.launch {
            announcementRepository.removeAnnouncement(announcement.id).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isRemoving = false,
                        message = exception.message ?: ""
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isRemoving = false,
                                message = "Remove succeeded"
                            )
                        }

                        _isShowSnackbar.emit(true)
                        fetchData()
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isRemoving = false,
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