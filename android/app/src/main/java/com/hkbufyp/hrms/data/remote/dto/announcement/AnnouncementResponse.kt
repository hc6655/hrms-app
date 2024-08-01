package com.hkbufyp.hrms.data.remote.dto.announcement

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementsResponse(
    val announcements: List<AnnouncementDto>
)

@Serializable
data class CreateAnnouncementResponse(
    val announcementId: String
)