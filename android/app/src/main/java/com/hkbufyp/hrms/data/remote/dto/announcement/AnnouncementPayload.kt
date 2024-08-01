package com.hkbufyp.hrms.data.remote.dto.announcement

import kotlinx.serialization.Serializable

@Serializable
data class CreateAnnouncementPayload(
    val title: String,
    val content: String,
    val isPushNotification: Boolean
)

@Serializable
data class UpdateAnnouncementPayload(
    val title: String,
    val content: String,
    val isPushNotification: Boolean
)