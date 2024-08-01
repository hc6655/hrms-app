package com.hkbufyp.hrms.data.remote.dto.announcement

import com.hkbufyp.hrms.domain.model.Announcement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDto(
    val id: Int,
    val title: String,
    val content: String,
    @SerialName("publish_date")
    val publishDate: String,
    @SerialName("publish_employee_id")
    val publishEmployeeId: String,
    @SerialName("is_push_notification")
    val isPushNotification: Int,
    val publisher: String
)

fun AnnouncementDto.toAnnouncement() =
    Announcement(
        id = id,
        title = title,
        content = content,
        publishDate = publishDate,
        publisher = publisher,
        publisherId = publishEmployeeId
    )