package com.hkbufyp.hrms.ui.screen.management.department.departments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.repository.DepartmentRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepartmentsViewModel(
    private val departmentRepository: DepartmentRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DepartmentsUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    fun onEvent(event: DepartmentsEvent) {
        when (event) {
            DepartmentsEvent.Enter -> fetchData()
            DepartmentsEvent.Refresh -> fetchData(true)
            is DepartmentsEvent.DeleteClicked -> {
                _uiState.update { state ->
                    state.copy(
                        isDeleteClicked = true,
                        currentDeleteDepartment = event.department
                    )
                }
            }
            DepartmentsEvent.DeleteConfirmed -> deleteDepartment()
            DepartmentsEvent.DeleteCancelled -> {
                _uiState.update { state ->
                    state.copy(isDeleteClicked = false)
                }
            }
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isLoadingData = !isRefresh,
                isRefreshing = isRefresh
            )
        }

        viewModelScope.launch {
            departmentRepository.getDepartments().catch { exception ->
                println(exception.message)
                _uiState.update { state ->
                    state.copy(
                        isLoadingData = false,
                        isRefreshing = false
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
                                departments = response.data ?: emptyList()
                            )
                        }
                    }
                    is NetworkResponse.Failure -> {
                        println(response.errMessage)
                        _uiState.update { state ->
                            state.copy(
                                isLoadingData = false,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteDepartment() {
        _uiState.update { state ->
            state.copy(
                isDeleteClicked = false,
                isLoadingData = true
            )
        }

        val department = uiState.value.currentDeleteDepartment
        if (department == null || department.id.isEmptyOrBlank()) {
            _uiState.update { state ->
                state.copy(
                    isLoadingData = false,
                    message = "Data error"
                )
            }
            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }
            return
        }

        viewModelScope.launch {
            departmentRepository.deleteDepartment(department.id).catch { exception ->
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
                                message = "Delete successful",
                                departments = state.departments.filter { it.id != department.id }
                            )
                        }
                        _isShowSnackbar.emit(true)
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
}