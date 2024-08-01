package com.hkbufyp.hrms.ui.screen.user.leave.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationPayload
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

class LeaveApplyViewModel(
    private val leaveRepository: LeaveRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveApplyUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _applicationId = MutableSharedFlow<Int>()
    val applicationId = _applicationId.asSharedFlow()

    init {
        fetchData()
    }

    fun onEvent(event: LeaveApplyEvent) {
        when (event) {
            LeaveApplyEvent.Apply -> {
                applyLeave()
            }
            LeaveApplyEvent.ShowDatePickerForStartDate -> {
                _uiState.update { state ->
                    state.copy(
                        isShowDatePickerForStartDate = true
                    )
                }
            }
            LeaveApplyEvent.ShowDatePickerForEndDate -> {
                _uiState.update { state ->
                    state.copy(
                        isShowDatePickerForEndDate = true
                    )
                }
            }
            LeaveApplyEvent.DismissDatePickerForStartDate -> {
                _uiState.update { state ->
                    state.copy(
                        isShowDatePickerForStartDate = false
                    )
                }
            }
            LeaveApplyEvent.DismissDatePickerForEndDate -> {
                _uiState.update { state ->
                    state.copy(
                        isShowDatePickerForEndDate = false
                    )
                }
            }
            LeaveApplyEvent.LeaveTypeExpandChanged -> {
                _uiState.update { state ->
                    state.copy(
                        isLeaveTypeExpand = !state.isLeaveTypeExpand
                    )
                }
            }

            is LeaveApplyEvent.SelectedStartDate -> {
                _uiState.update { state ->
                    state.copy(
                        startDate = event.date
                    )
                }
            }

            is LeaveApplyEvent.SelectedEndDate -> {
                _uiState.update { state ->
                    state.copy(
                        endDate = event.date
                    )
                }
            }

            is LeaveApplyEvent.ApplyDaysChanged -> {
                _uiState.update { state ->
                    state.copy(
                        applyDays = event.days
                    )
                }
            }

            is LeaveApplyEvent.ReasonChanged -> {
                _uiState.update { state ->
                    state.copy(
                        reason = event.reason
                    )
                }
            }

            is LeaveApplyEvent.SelectedLeaveType -> {
                _uiState.update { state ->
                    state.copy(
                        selectedLeaveType = event.leaveType,
                        isLeaveTypeExpand = false
                    )
                }
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            leaveRepository.getLeaveTypes().catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
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
                                leaveTypes = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                message = response.errMessage ?: "Unknown error"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun applyLeave() {
        _uiState.update { state ->
            state.copy(isApplying = true)
        }

        val startDate = _uiState.value.startDate
        val endDate = _uiState.value.endDate
        val days = _uiState.value.applyDays.toFloatOrNull()
        val leaveTypeId = _uiState.value.selectedLeaveType?.id ?: 0
        val reason = _uiState.value.reason
        var isDataValid = true
        var errorMessage = ""

        if (startDate.isEmptyOrBlank() ||
            endDate.isEmptyOrBlank()) {
            isDataValid = false
            errorMessage = "Some data are missing"
        }

        if (days == null || days < 0.5f) {
            isDataValid = false
            errorMessage = "The apply days should greater than 0.5 day"
        }

        if (_uiState.value.selectedLeaveType?.isNeedReasonForApply == true &&
            reason.isEmptyOrBlank()) {
            isDataValid = false
            errorMessage = "You should type in the reason of this application"
        }

        if (!isDataValid) {
            _uiState.update { state ->
                state.copy(
                    isApplying = false,
                    message = errorMessage
                )
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        viewModelScope.launch {
            leaveRepository.createLeaveApplication(
                LeaveApplicationPayload(
                    startDate = startDate,
                    endDate = endDate,
                    days = days!!,
                    leaveTypeId = leaveTypeId,
                    reason = reason
                ),
                employeeId
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isApplying = false,
                        message = exception.message ?: "Unknown exception"
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isApplying = false,
                                message = "Apply successful, please wait for approval"
                            )
                        }
                        _isShowSnackbar.emit(true)
                        _applicationId.emit(response.data ?: 0)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isApplying = false,
                                message = response.errMessage ?: "Unknown error"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}