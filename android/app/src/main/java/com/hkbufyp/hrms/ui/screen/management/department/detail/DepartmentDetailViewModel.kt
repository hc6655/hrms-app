package com.hkbufyp.hrms.ui.screen.management.department.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.department.UpdateDepartmentPayload
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.DepartmentDetail
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.repository.DepartmentRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepartmentDetailViewModel(
    private val departmentRepository: DepartmentRepository,
    private val departmentId: String?
): ViewModel() {

    private val _uiState = MutableStateFlow(DepartmentDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _isSubmitSucceed = MutableSharedFlow<Boolean>()
    val isSubmitSucceed = _isSubmitSucceed.asSharedFlow()

    private val _isModified = mutableStateOf(false)

    private val removedPosition: MutableList<Int> = mutableListOf()
    private val updatedPosition: HashMap<Int, Position> = hashMapOf()

    init {
        if (departmentId != "null") {
            _uiState.update { state ->
                state.copy(isCreation = false)
            }

            fetchData()
        } else {
            _uiState.update { state ->
                state.copy(
                    department = DepartmentDetail(
                        id = "",
                        title = "",
                        positions = listOf(
                            Position(
                                id = 0,
                                departmentId = "",
                                name = "",
                                accessLevel = 0,
                                managementFeature = false,
                                accessLog = false
                            )
                        )
                    ),
                    isAccessLevelExpanded = listOf(false)
                )
            }
        }

        val accessList = mutableListOf<String>()
        for (i in 1..10) {
            accessList.add("Access Level $i")
        }

        _uiState.update { state ->
            state.copy(accessLevelList = accessList)
        }
    }

    fun onEvent(event: DepartmentDetailEvent) {
        when (event) {
            DepartmentDetailEvent.Submit -> {
                updateDepartment()
            }
            DepartmentDetailEvent.AddPosition -> addPosition()
            is DepartmentDetailEvent.AccessLevelExpand -> {
                _uiState.update { state ->
                    state.copy(
                        isAccessLevelExpanded = state.isAccessLevelExpanded.mapIndexed { index, b ->
                            var expanded = b
                            if (index == event.index) {
                                expanded = !expanded
                            }

                            expanded
                        }
                    )
                }
            }
            is DepartmentDetailEvent.TitleChanged -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            title = event.title
                        )
                    )
                }
                _isModified.value = true
            }
            is DepartmentDetailEvent.IdChanged -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            id = event.id
                        )
                    )
                }
            }
            is DepartmentDetailEvent.RemovePosition -> deletePosition(event.index)
            is DepartmentDetailEvent.SetPositionName -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            positions = state.department.positions.mapIndexed { index, position ->
                                var name = position.name
                                if (index == event.index) {
                                    name = event.name
                                    if (!state.isCreation && position.id > 0) {
                                        updatedPosition[position.id] = position.copy(name = name)
                                    }
                                }

                                position.copy(name = name)
                            }
                        )
                    )
                }
            }
            is DepartmentDetailEvent.SetAccessLevel -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            positions = state.department.positions.mapIndexed { index, position ->
                                var level = position.accessLevel
                                if (index == event.index) {
                                    level = event.level
                                    if (!state.isCreation && position.id > 0) {
                                        updatedPosition[position.id] = position.copy(accessLevel = level)
                                    }
                                }

                                position.copy(accessLevel = level)
                            }
                        )
                    )
                }
            }
            is DepartmentDetailEvent.SetManagement -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            positions = state.department.positions.mapIndexed { index, position ->
                                var on = position.managementFeature
                                if (index == event.index) {
                                    on = event.on
                                    if (!state.isCreation && position.id > 0) {
                                        updatedPosition[position.id] = position.copy(managementFeature = on)
                                    }
                                }

                                position.copy(managementFeature = on)
                            }
                        )
                    )
                }
            }
            is DepartmentDetailEvent.SetAccessLog -> {
                _uiState.update { state ->
                    state.copy(
                        department = state.department?.copy(
                            positions = state.department.positions.mapIndexed { index, position ->
                                var on = position.accessLog
                                if (index == event.index) {
                                    on = event.on
                                    if (!state.isCreation && position.id > 0) {
                                        updatedPosition[position.id] = position.copy(accessLog = on)
                                    }
                                }

                                position.copy(accessLog = on)
                            }
                        )
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
            if (departmentId != null) {
                departmentRepository.getDepartment(departmentId).catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isLoadingData = false,
                            message = exception.message ?: "Unknown Exception"
                        )
                    }

                    _isShowSnackbar.emit(true)
                }.collect { response ->
                    when (response) {
                        is NetworkResponse.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    isLoadingData = false,
                                    department = response.data,
                                    isAccessLevelExpanded = List(size = response.data?.positions?.size ?: 0) { false }
                                )
                            }
                        }

                        is NetworkResponse.Failure -> {
                            _uiState.update { state ->
                                state.copy(
                                    isLoadingData = false,
                                    message = response.errMessage ?: "Unknown Exception"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateDepartment() {
        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        val department = _uiState.value.department
        if (department == null ||
            department.id.isEmptyOrBlank() ||
            department.title.isEmptyOrBlank()) {
            _uiState.update { state ->
                state.copy(
                    message = "Some data are missing",
                    isSubmitting = false
                )
            }
            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }
            return
        }

        if (department.positions.isEmpty()) {
            _uiState.update { state ->
                state.copy(
                    message = "Please provide at least 1 position",
                    isSubmitting = false
                )
            }
            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }
            return
        }

        val addedList = mutableListOf<Position>()
        department.positions.forEach { position ->
            if (position.name.isEmptyOrBlank()) {
                _uiState.update { state ->
                    state.copy(
                        message = "Please fill in the position name",
                        isSubmitting = false
                    )
                }
                viewModelScope.launch {
                    _isShowSnackbar.emit(true)
                }
                return
            }

            if (!_uiState.value.isCreation && position.id <= 0) {
                addedList.add(position)
            }
        }

        viewModelScope.launch {
            if (_uiState.value.isCreation) {
                departmentRepository.createDepartment(department).catch { exception ->
                    catchRequest(exception)
                }.collect { response ->
                    collectRequest(response)
                }
            } else {
                departmentRepository.updateDepartment(
                    department,
                    UpdateDepartmentPayload(
                        title = department.title,
                        deletedPositions = removedPosition,
                        updatedPositions = updatedPosition.values.toList(),
                        addedPositions = addedList
                    )
                ).catch { exception ->
                    catchRequest(exception)
                }.collect { response ->
                    collectRequest(response)
                }
            }
        }
    }

    private suspend fun catchRequest(exception: Throwable) {
        _uiState.update { state ->
            state.copy(
                isSubmitting = false,
                message = exception.message ?: "Unknown Exception"
            )
        }
        _isShowSnackbar.emit(true)
    }

    private suspend fun collectRequest(response: NetworkResponse<String>) {
        when (response) {
            is NetworkResponse.Success -> {
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = if (uiState.value.isCreation) "Create successful" else "Update successful"
                    )
                }
                _isShowSnackbar.emit(true)
                _isSubmitSucceed.emit(true)
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

    private fun addPosition() {
        val position = Position(
            id = 0,
            departmentId = _uiState.value.department?.id ?: "",
            name = "",
            accessLevel = 0,
            managementFeature = false,
            accessLog = false
        )

        _uiState.update { state ->
            state.copy(
                department = state.department!!.copy(
                    positions = state.department.positions + listOf(position)
                ),
                isAccessLevelExpanded = state.isAccessLevelExpanded + listOf(false)
            )
        }
    }

    private fun deletePosition(index: Int) {
        val position = _uiState.value.department?.positions?.get(index) ?: return

        if (!_uiState.value.isCreation && position.id > 0) {
            removedPosition.add(position.id)
        }

        _uiState.update { state ->
            state.copy(
                department = state.department!!.copy(
                    positions = state.department.positions - state.department.positions[index]
                ),
                isAccessLevelExpanded = state.isAccessLevelExpanded - state.isAccessLevelExpanded[index]
            )
        }
    }
}