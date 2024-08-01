package com.hkbufyp.hrms.ui.screen.management.announcement.manage

import com.hkbufyp.hrms.domain.model.Announcement

data class ManageAnnouncementUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val isRemoving: Boolean = false,
    val message: String = "",
    val isShowAlert: Boolean = false,
    val removingAnnounce: Announcement? = null,
    val announcements: List<Announcement>? = null
)
