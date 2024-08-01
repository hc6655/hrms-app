package com.hkbufyp.hrms.ui.screen.management.employee.attend_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.attendance.GetAttendanceRecordPayload
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.getEndOfWeek
import com.hkbufyp.hrms.util.getStartOfWeek
import com.hkbufyp.hrms.util.isEmptyOrBlank
import com.hkbufyp.hrms.util.toFormatStringByDate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent
import java.util.Calendar

class EmployeeAttendRecordViewModel(
    private val attendRepository: AttendRepository,
    private val userRepository: UserRepository,
    private val employeeId: String
):ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeAttendRecordUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    init {
        initCalendar()
        fetchData()
    }

    fun onEvent(event: EmployeeAttendRecordEvent) {
        when (event) {
            EmployeeAttendRecordEvent.Previous -> prevMonth()
            EmployeeAttendRecordEvent.Next -> nextMonth()
        }
    }

    private fun initCalendar() {
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        endCalendar.add(Calendar.MONTH, 1)
        endCalendar.set(Calendar.DAY_OF_MONTH, 1)
        endCalendar.add(Calendar.DATE, -1)

        setDateString()
    }

    private fun setDateString() {
        _uiState.update { state ->
            state.copy(
                startDate = startCalendar.toFormatStringByDate(),
                endDate = endCalendar.toFormatStringByDate()
            )
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        var errMessage = ""

        viewModelScope.launch {
            try {
                fetchUser()
                fetchAttendance()
                calculateBalance()
            } catch (e: Exception) {
                errMessage = e.message ?: "Unknown exception."
            }

            _uiState.update { state ->
                state.copy(
                    isLoadingData = false,
                    message = errMessage
                )
            }

            if (!errMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private suspend fun fetchUser() {
        userRepository.getUserInfo(employeeId).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(user = response.data)
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchAttendance() {
        attendRepository.getAttendanceRecord(
            employeeId = employeeId,
            payload = GetAttendanceRecordPayload(
                startDate = startCalendar.toFormatStringByDate(),
                endDate = endCalendar.toFormatStringByDate()
            )
        ).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(attendRecord = response.data)
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun calculateBalance() {
        val attendRecord = _uiState.value.attendRecord ?: return
        val user = _uiState.value.user ?: return

        val salary = user.salary
        var lateBalance = 0f
        var earlyLeaveBalance = 0f
        var overtimeBalance = 0f

        attendRecord.brief.forEach { brief ->
            val hourlySalary = salary / 30f / brief.workingHours
            if (brief.lateHour > 0.0f) {
                lateBalance += (hourlySalary * brief.lateHour)
            }
            if (brief.overtimeHour > 0.0f) {
                overtimeBalance += (hourlySalary * brief.overtimeHour)
            }
            if (brief.earlyLeaveHour > 0.0f) {
                earlyLeaveBalance += (hourlySalary * brief.earlyLeaveHour)
            }
        }

        val finalSalary = salary - lateBalance - earlyLeaveBalance + overtimeBalance

        _uiState.update { state ->
            state.copy(
                lateBalance = "-%.2f".format(lateBalance),
                earlyLeaveBalance = "-%.2f".format(earlyLeaveBalance),
                overtimeBalance = "+%.2f".format(overtimeBalance),
                salary = "$%.2f".format(finalSalary)
            )
        }
    }

    private fun prevMonth() {
        startCalendar.add(Calendar.MONTH, -1)
        endCalendar.add(Calendar.MONTH, -1)

        initCalendar()
        setDateString()
        fetchData()
    }

    private fun nextMonth() {
        startCalendar.add(Calendar.MONTH, 1)
        endCalendar.add(Calendar.MONTH, 1)

        initCalendar()
        setDateString()
        fetchData()
    }
}