package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.AttendTimeslot
import com.hkbufyp.hrms.domain.model.AttendWorkingTimeslot
import com.hkbufyp.hrms.domain.model.ValidClockInRange
import com.hkbufyp.hrms.domain.model.toDto
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.toCalendar24
import com.hkbufyp.hrms.util.toFormatStringByTime12
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendTimeslotDetailViewModel(
    private val attendRepository: AttendRepository,
    private val slotId: Int
): ViewModel() {

    private val _uiState = MutableStateFlow(AttendTimeslotDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isSubmitSucceeded = MutableSharedFlow<Boolean>()
    val isSubmitSucceeded = _isSubmitSucceeded.asSharedFlow()

    init {
        if (slotId > 0) {
            _uiState.update { state ->
                state.copy(isCreate = false)
            }

            fetchData()
        } else {
            _uiState.update { state ->
                state.copy(
                    attendTimeslot = AttendTimeslot(
                        name = "",
                        slots = emptyList(),
                        requireClockIn = true,
                        requireClockOut = true,
                        countAbsent = true,
                        countLate = true,
                        countLeaveEarly = true,
                        countOvertime = true,
                        totalWorkingHours = 0f,
                        latenessGrace = 0,
                        earlyLeaveGrace = 0,
                        overtimeStart = 0,
                        validClockInRange = ValidClockInRange.Automatic,
                        rangeOfBeforeWork = 60,
                        rangeOfAfterWork = 600,
                        rangeOfBeforeLeave = -1,
                        rangeOfAfterLeave = -1
                    )
                )
            }

            addSlot()
        }
    }

    fun onEvent(event: AttendTimeslotDetailEvent) {
        when (event) {
            AttendTimeslotDetailEvent.Submit -> submit()
            AttendTimeslotDetailEvent.AddSlot -> addSlot()
            is AttendTimeslotDetailEvent.RemoveSlot -> removeSlot(event.index)
            is AttendTimeslotDetailEvent.SetName -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            name = event.name
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetCountAbsent -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            countAbsent = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetCountLate -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            countLate = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetCountLeaveEarly -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            countLeaveEarly = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetCountOvertime -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            countOvertime = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetTotalWorkingHours -> {
                val f = event.v.toFloatOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            totalWorkingHours = f
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetEarlyLeaveGrace -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            earlyLeaveGrace = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetOvertimeStart -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            overtimeStart = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetEndTime -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            slots = state.attendTimeslot.slots.mapIndexed { index, attendWorkingTimeslot ->
                                if (index == event.index) {
                                    attendWorkingTimeslot.end = event.time
                                }

                                attendWorkingTimeslot
                            }
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetStartTime -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            slots = state.attendTimeslot.slots.mapIndexed { index, attendWorkingTimeslot ->
                                if (index == event.index) {
                                    attendWorkingTimeslot.start = event.time
                                }

                                attendWorkingTimeslot
                            }
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetLatenessGrace -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            latenessGrace = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRangeOfAfterLeave -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            rangeOfAfterLeave = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRangeOfAfterWork -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            rangeOfAfterWork = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRangeOfBeforeLeave -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            rangeOfBeforeLeave = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRangeOfBeforeWork -> {
                val value = event.v.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            rangeOfBeforeWork = value
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRequireClockIn -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            requireClockIn = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetRequireClockOut -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            requireClockOut = event.isCheck
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.SetValidRangeMode -> {
                _uiState.update { state ->
                    state.copy(
                        attendTimeslot = state.attendTimeslot?.copy(
                            validClockInRange = event.mode
                        )
                    )
                }
            }
            is AttendTimeslotDetailEvent.ShowSelectRangeDialog -> {
                _uiState.update { state ->
                    state.copy(showSelectRangeDialog = event.isShow)
                }
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            attendRepository.getTimeslotById(slotId).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
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
                                attendTimeslot = response.data
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                message = response.errMessage ?: ""
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }

    private fun addSlot() {
        _uiState.update { state ->
            state.copy(
                attendTimeslot = state.attendTimeslot?.copy(
                    slots = state.attendTimeslot.slots +
                            listOf(
                                AttendWorkingTimeslot(
                                    0,
                                    "09:00".toCalendar24().toFormatStringByTime12(),
                                    "18:00".toCalendar24().toFormatStringByTime12())
                            )
                )
            )
        }
    }

    private fun removeSlot(index: Int) {
        _uiState.update { state ->
            state.copy(
                attendTimeslot = state.attendTimeslot?.copy(
                    slots = state.attendTimeslot.slots - state.attendTimeslot.slots[index]
                )
            )
        }
    }

    private fun submit() {
        val dto = _uiState.value.attendTimeslot?.toDto()
        if (dto == null) {
            _uiState.update { state ->
                state.copy(
                    isSubmitting = false,
                    message = "Internal error."
                )
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        if (_uiState.value.isCreate) {
            viewModelScope.launch {
                attendRepository.createTimeslot(dto).catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            message = exception.message ?: ""
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
                            _isSubmitSucceeded.emit(true)
                        }
                        is NetworkResponse.Failure -> {
                            _uiState.update { state ->
                                state.copy(
                                    isSubmitting = false,
                                    message = response.errMessage ?: ""
                                )
                            }

                            _isShowSnackbar.emit(true)
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                attendRepository.updateTimeslot(dto).catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            message = exception.message ?: ""
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
                            _isSubmitSucceeded.emit(true)
                        }
                        is NetworkResponse.Failure -> {
                            _uiState.update { state ->
                                state.copy(
                                    isSubmitting = false,
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
}