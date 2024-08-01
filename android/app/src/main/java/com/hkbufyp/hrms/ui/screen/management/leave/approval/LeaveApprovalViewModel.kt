package com.hkbufyp.hrms.ui.screen.management.leave.approval

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.leave.UpdateLeaveStatusPayload
import com.hkbufyp.hrms.domain.enums.LeaveApplicationStatus
import com.hkbufyp.hrms.domain.enums.toInt
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveApprovalViewModel(
    private val leaveRepository: LeaveRepository,
    private val applicationId: Int
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveApprovalUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        fetchData()
    }

    fun onEvent(event: LeaveApprovalEvent) {
        when (event) {
            LeaveApprovalEvent.Refresh -> {
                fetchData(true)
            }
            LeaveApprovalEvent.Approve -> {
                updateLeaveStatus(LeaveApplicationStatus.APPROVED)
            }
            LeaveApprovalEvent.Reject -> {
                updateLeaveStatus(LeaveApplicationStatus.REJECTED)
            }
            LeaveApprovalEvent.ShowDialog -> {
                showDialog(true)
            }
            LeaveApprovalEvent.HideDialog -> {
                showDialog(false)
            }
            is LeaveApprovalEvent.ReasonChanged -> {
                onReasonChanged(event.v)
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
                _isShowSnackbar.emit(true)
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
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun updateLeaveStatus(status: LeaveApplicationStatus) {
        when (status) {
            LeaveApplicationStatus.APPROVED -> {
                _uiState.update { state ->
                    state.copy(isApproving = true)
                }
            }
            LeaveApplicationStatus.REJECTED -> {
                _uiState.update { state ->
                    state.copy(
                        isRejecting = true,
                        isShowDialog = false
                    )
                }
            }
            else -> {
                return
            }
        }

        if (status == LeaveApplicationStatus.REJECTED) {
            if (_uiState.value.reason.isEmptyOrBlank()) {
                _uiState.update { state ->
                    state.copy(
                        isRejecting = false,
                        message = "Please provide a reason"
                    )
                }

                viewModelScope.launch {
                    _isShowSnackbar.emit(true)
                }

                return
            }
        }

        viewModelScope.launch {
            leaveRepository.updateApplicationStatus(
                id = applicationId,
                payload = UpdateLeaveStatusPayload(status.toInt(), _uiState.value.reason)
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isApproving = false,
                        isRejecting = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isApproving = false,
                                isRejecting = false,
                                message = when (status) {
                                    LeaveApplicationStatus.APPROVED -> "Approved"
                                    LeaveApplicationStatus.REJECTED -> "Rejected"
                                    else -> "Succeed with unknown status"
                                },
                                leaveApplication = state.leaveApplication?.copy(
                                    status = status
                                )
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isApproving = false,
                                isRejecting = false,
                                message = response.errMessage ?: "Unknown exception"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun showDialog(isShow: Boolean) {
        _uiState.update { state ->
            state.copy(isShowDialog = isShow)
        }

        if (!isShow) {
            onReasonChanged("")
        }
    }

    private fun onReasonChanged(v: String) {
        _uiState.update { state ->
            state.copy(reason = v)
        }
    }
}