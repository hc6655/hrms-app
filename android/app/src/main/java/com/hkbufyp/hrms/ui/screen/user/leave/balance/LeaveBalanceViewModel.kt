package com.hkbufyp.hrms.ui.screen.user.leave.balance

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

class LeaveBalanceViewModel(
    private val leaveRepository: LeaveRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(LeaveBalanceUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    init {
        getStatus()
    }

    private fun getStatus() {
        _uiState.update {
            it.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            leaveRepository.getUserLeaveBalance(employeeId).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        message = exception.message ?: ""
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect {
                when(it) {
                    is NetworkResponse.Success -> {
                        println(it.data)
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                balances = it.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                message = it.errMessage ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}