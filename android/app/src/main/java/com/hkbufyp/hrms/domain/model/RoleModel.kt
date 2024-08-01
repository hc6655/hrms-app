package com.hkbufyp.hrms.domain.model

data class Role(
    val id: Int,
    val title: String,
    val department: Department? = null,
    val permission: Permission? = null
)