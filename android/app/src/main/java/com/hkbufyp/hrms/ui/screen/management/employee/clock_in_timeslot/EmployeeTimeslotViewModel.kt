package com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.attendance.AddCustomShiftPayload
import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EmployeeTimeslotViewModel(
    private val attendRepository: AttendRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeTimeslotUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnacknar = _isShowSnackbar.asSharedFlow()

    private var _customShifts = listOf<AttendCustomShift>()

    init {
        fetchData()
    }

    fun onEvent(event: EmployeeTimeslotEvent) {
        when (event) {
            EmployeeTimeslotEvent.AddShift -> {
                _uiState.update { state ->
                    state.copy(isShowAddDialog = true)
                }
            }
            is EmployeeTimeslotEvent.ConfirmAdd -> {
                _uiState.update { state ->
                    state.copy(selectedSlot = event.slot)
                }
                confirmAdd()
            }
            EmployeeTimeslotEvent.ConfirmRemove -> {
                confirmRemove()
            }
            EmployeeTimeslotEvent.CancelAdd -> {
                _uiState.update { state ->
                    state.copy(
                        isShowAddDialog = false,
                        selectedSlot = null
                    )
                }
            }
            EmployeeTimeslotEvent.CancelRemove -> {
                _uiState.update { state ->
                    state.copy(
                        isShowRemoveDialog = false,
                        removingShift = null
                    )
                }
            }
            is EmployeeTimeslotEvent.SelectDate -> {
                _uiState.update { state ->
                    state.copy(
                        selectedDate = event.date,
                        shifts = _customShifts.filter { it.date == event.date.toString() }
                    )
                }
            }
            is EmployeeTimeslotEvent.RemoveShift -> {
                _uiState.update { state ->
                    state.copy(
                        isShowRemoveDialog = true,
                        removingShift = event.shift
                    )
                }
            }
        }
    }

    private fun fetchData() {
        if (employeeId.isEmptyOrBlank()) {
            return
        }

        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            var errorMessage = ""

            try {
                fetchSlot()
                fetchShift()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown Exception"
            }

            _uiState.update { state ->
                state.copy(
                    isLoadingData = false,
                    message = errorMessage,
                    selectedDate = LocalDate.now(),
                    shifts = _customShifts.filter { it.date == LocalDate.now().toString() }
                )
            }

            if (!errorMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private fun fetchShiftOnly() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            attendRepository.getCustomShift(employeeId).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: "Unknown Exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when(response) {
                    is NetworkResponse.Success -> {
                        _customShifts = response.data ?: emptyList()
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                allShifts = response.data ?: emptyList(),
                                shifts = _customShifts.filter { it.date == state.selectedDate?.toString() }
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: "Unknown Exception"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private suspend fun fetchSlot() {
        attendRepository.getTimeslotBrief().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    println(response.data)
                    _uiState.update { state ->
                        state.copy(
                            timeslots = response.data ?: emptyList(),
                        )
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchShift() {
        attendRepository.getCustomShift(employeeId).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    _customShifts = response.data ?: emptyList()
                    _uiState.update { state ->
                        state.copy(allShifts = response.data ?: emptyList())
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun confirmAdd() {
        _uiState.update { state ->
            state.copy(isShowAddDialog = false)
        }

        if (_customShifts.isNotEmpty()) {
            _uiState.update { state ->
                state.copy(message = "Custom shift is already exist")
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        val selectedDate = _uiState.value.selectedDate
        val selectedSlot = _uiState.value.selectedSlot

        if (selectedDate == null || selectedSlot == null) {
            _uiState.update { state ->
                state.copy(message = "Data error")
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
            attendRepository.addCustomShift(
                employeeId = employeeId,
                payload = AddCustomShiftPayload(
                    date = selectedDate.toString(),
                    timeslotId = selectedSlot.id
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: "Unknown Exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Succeeded"
                            )
                        }

                        _isShowSnackbar.emit(true)
                        fetchShiftOnly()
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: "Unknown Exception"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun confirmRemove() {
        _uiState.update { state ->
            state.copy(isShowRemoveDialog = false)
        }

        val shift = _uiState.value.removingShift
        if (shift == null) {
            _uiState.update { state ->
                state.copy(message = "Please select a shifting to remove")
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
            attendRepository.removeCustomShift(shift.id).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: "Unknown Exception"
                    )
                }

                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Succeeded"
                            )
                        }

                        _isShowSnackbar.emit(true)
                        fetchShiftOnly()
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: "Unknown Exception"
                            )
                        }

                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}