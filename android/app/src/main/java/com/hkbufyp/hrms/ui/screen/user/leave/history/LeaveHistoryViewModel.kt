package com.hkbufyp.hrms.ui.screen.user.leave.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveHistoryViewModel(
    private val leaveRepository: LeaveRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveHistoryUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LeaveHistoryEvent) {
        when (event) {
            is LeaveHistoryEvent.Refresh -> {
                _uiState.update { state ->
                    state.copy(isRefreshing = true)
                }
                fetchData()
            }
            is LeaveHistoryEvent.Enter -> {
                fetchData()
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(
                isLoadingData = true,
                leaveApplications = MutableList(10) { null }
            )
        }

        viewModelScope.launch {
            leaveRepository.getApplicationsByEmployeeId(employeeId).catch { exception ->
                _uiState.update { state ->
                    println(exception.message)
                    state.copy(
                        isLoadingData = false,
                        isRefreshing = false,
                        message = exception.message ?: "",
                        leaveApplications = emptyList()
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
                                message = response.errMessage ?: "",
                                leaveApplications = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }
}