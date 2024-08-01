package com.hkbufyp.hrms.ui.screen.management.department.detail

sealed class DepartmentDetailEvent {
    data object Submit: DepartmentDetailEvent()
    data object AddPosition: DepartmentDetailEvent()
    data class AccessLevelExpand(val index: Int): DepartmentDetailEvent()
    data class TitleChanged(val title: String): DepartmentDetailEvent()
    data class IdChanged(val id: String): DepartmentDetailEvent()
    data class RemovePosition(val index: Int): DepartmentDetailEvent()
    data class SetPositionName(val index: Int, val name: String): DepartmentDetailEvent()
    data class SetAccessLevel(val index: Int, val level: Int): DepartmentDetailEvent()
    data class SetManagement(val index: Int, val on: Boolean): DepartmentDetailEvent()
    data class SetAccessLog(val index: Int, val on: Boolean): DepartmentDetailEvent()
}
