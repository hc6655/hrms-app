package com.hkbufyp.hrms.ui.screen.announcement

import com.hkbufyp.hrms.domain.model.Announcement

data class AnnounceUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val announcements: List<Announcement> = emptyList(),
    val message: String = ""
)
