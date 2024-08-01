package com.hkbufyp.hrms.ui.screen.user.leave.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveDetailViewModel(
    private val leaveRepository: LeaveRepository,
    private val applicationId: Int
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun onEvent(event: LeaveDetailEvent) {
        when (event) {
            LeaveDetailEvent.Refresh -> {
                _uiState.update { state ->
                    state.copy(isRefreshing = true)
                }
                fetchData(true)
            }
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        if (!isRefresh) {
            _uiState.update { state ->
                state.copy(isLoadingData = true)
            }
        }

        viewModelScope.launch {
            leaveRepository.getApplicationById(applicationId).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        isRefreshing = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        println(response.data)
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                isRefreshing = false,
                                leaveApplication = response.data
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                isRefreshing = false,
                                message = response.errMessage ?: "Unknown error"
                            )
                        }
                    }
                }
            }
        }
    }
}