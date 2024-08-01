package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendTimeslotViewModel(
    private val attendRepository: AttendRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AttendTimeslotUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AttendTimeslotEvent) {
        when (event) {
            AttendTimeslotEvent.Enter -> fetchData()
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            attendRepository.getTimeslot().catch { exception ->
                _uiState.update { state ->
                    state.copy(isLoadingData = false)
                }
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        println(response.data)
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                timeslots = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(isLoadingData = false)
                        }
                    }
                }
            }
        }
    }
}