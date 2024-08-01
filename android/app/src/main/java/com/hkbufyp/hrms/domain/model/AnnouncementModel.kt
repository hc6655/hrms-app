package com.hkbufyp.hrms.domain.model

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val publishDate: String,
    val publisher: String,
    val publisherId: String
)