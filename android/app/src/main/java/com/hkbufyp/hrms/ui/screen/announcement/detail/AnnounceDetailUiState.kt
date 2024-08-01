package com.hkbufyp.hrms.ui.screen.announcement.detail

import com.hkbufyp.hrms.domain.model.Announcement

data class AnnounceDetailUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val announcement: Announcement? = null,
    val message: String = ""
)
