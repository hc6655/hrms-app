package com.hkbufyp.hrms.ui.screen.user.announcement

import com.hkbufyp.hrms.domain.model.Announcement

data class AnnouncementUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val message: String = "",
    val announcements: List<Announcement>? = null
)
