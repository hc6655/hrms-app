package com.hkbufyp.hrms.domain.model

data class Permission(
    val accessLevel: Int,
    val managementFeature: Boolean,
    val accessLog: Boolean
)
