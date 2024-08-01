package com.hkbufyp.hrms.ui.screen.management.leave.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveApplicationsViewModel(
    private val leaveRepository: LeaveRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveApplicationsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LeaveApplicationsEvent) {
        when (event) {
            LeaveApplicationsEvent.Refresh -> {
                _uiState.update { state ->
                    state.copy(isRefreshing = true)
                }
                fetchData(true)
            }
            LeaveApplicationsEvent.Enter -> {
                fetchData()
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
            leaveRepository.getApplications().catch { exception ->
                _uiState.update { state ->
                    println(exception.message)
                    state.copy(
                        isLoadingData = false,
                        isRefreshing = false,
                        message = exception.message ?: ""
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
                                leaveApplications = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        println(response.errMessage)
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                isRefreshing = false,
                                message = response.errMessage ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}