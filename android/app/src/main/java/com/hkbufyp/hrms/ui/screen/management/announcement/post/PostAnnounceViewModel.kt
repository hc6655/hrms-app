package com.hkbufyp.hrms.ui.screen.management.announcement.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.announcement.CreateAnnouncementPayload
import com.hkbufyp.hrms.data.remote.dto.announcement.UpdateAnnouncementPayload
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostAnnounceViewModel(
    private val announcementRepository: AnnouncementRepository,
    private val id: Int
): ViewModel() {

    private val _uiState = MutableStateFlow(PostAnnounceUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _announcementId = MutableSharedFlow<Int>()
    val announcementId = _announcementId.asSharedFlow()

    private val _focusRequest = MutableSharedFlow<Boolean>()
    val focusRequest = _focusRequest.asSharedFlow()

    init {
        if (id > 0) {
            _uiState.update { state ->
                state.copy(isCreate = false)
            }

            fetchData()
        } else {
            viewModelScope.launch {
                _focusRequest.emit(true)
            }
        }
    }

    fun onEvent(event: PostAnnounceEvent) {
        when (event) {
            PostAnnounceEvent.Submit -> {
                _uiState.update { state ->
                    state.copy(isShowAlertBox = true)
                }
            }
            PostAnnounceEvent.DismissDialog -> {
                _uiState.update { state ->
                    state.copy(isShowAlertBox = false)
                }
            }
            PostAnnounceEvent.Publish -> {
                _uiState.update { state ->
                    state.copy(isShowAlertBox = false)
                }
                if (_uiState.value.isCreate) {
                    postAnnouncement()
                } else {
                    updateAnnounce()
                }
            }
            is PostAnnounceEvent.TitleChanged -> {
                _uiState.update { state ->
                    state.copy(title = event.title)
                }
            }
            is PostAnnounceEvent.ContentChanged -> {
                _uiState.update { state ->
                    state.copy(content = event.content)
                }
            }
            is PostAnnounceEvent.CheckStateChanged -> {
                _uiState.update { state ->
                    state.copy(isPushNotification = event.checked)
                }
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            announcementRepository.getAnnouncement(id).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        message = exception.message ?: "Unknown Exception"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        val announce = response.data!!
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                title = announce.title,
                                content = announce.content
                            )
                        }

                        //_focusRequest.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                message = response.errMessage ?: "Unknown Exception"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun postAnnouncement() {
        val title = _uiState.value.title
        val content = _uiState.value.content
        val isPushNotification = _uiState.value.isPushNotification

        if (title.isEmptyOrBlank() || content.isEmptyOrBlank()) {
            _uiState.update { state ->
                state.copy(message = "Some data are missing")
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
            announcementRepository.createAnnouncement(
                CreateAnnouncementPayload(
                    title = title,
                    content = content,
                    isPushNotification = isPushNotification
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = "Could not publish an announcement"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                isShowAppBar = false,
                                message = "Publish announcement succeed"
                            )
                        }
                        _isShowSnackbar.emit(true)
                        _announcementId.emit(response.data ?: 0)
                    }
                    else -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Could not publish an announcement"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun updateAnnounce() {
        val title = _uiState.value.title
        val content = _uiState.value.content
        val isPushNotification = _uiState.value.isPushNotification

        if (title.isEmptyOrBlank() || content.isEmptyOrBlank()) {
            _uiState.update { state ->
                state.copy(message = "Some data are missing")
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
            announcementRepository.updateAnnouncement(
                id = id,
                payload = UpdateAnnouncementPayload(
                    title = title,
                    content = content,
                    isPushNotification = isPushNotification
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = "Could not publish an announcement"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                isShowAppBar = false,
                                message = "Publish announcement succeed"
                            )
                        }
                        _isShowSnackbar.emit(true)
                        _announcementId.emit(id)
                    }
                    else -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Could not publish an announcement"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}