package com.hkbufyp.hrms.ui.screen.management.employee.employees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeesViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(EmployeesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun onEvent(event: EmployeesEvent) {
        when (event) {
            is EmployeesEvent.TriggerSearch -> {
                _uiState.update { state ->
                    state.copy(
                        isSearching = event.isSearch
                    )
                }
            }
            is EmployeesEvent.SearchTextChanged -> {
                _uiState.update { state ->
                    state.copy(
                        searchText = event.text
                    )
                }
            }
            EmployeesEvent.PerformSearch -> {

            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        viewModelScope.launch {
            userRepository.getEmployees().catch { exception ->
                _uiState.update { state ->
                    state.copy(isLoadingData = false)
                }
            }.collect { response ->
                when(response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                employees = response.data ?: emptyList(),
                                employeesGroup = response.data?.groupBy { it.department }?.values ?: emptyList()
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