package com.hkbufyp.hrms.ui.screen.management.announcement.post

data class PostAnnounceUiState(
    val isSubmitting: Boolean = false,
    val title: String = "",
    val content: String = "",
    val message: String = "",
    val isShowAlertBox: Boolean = false,
    val isPushNotification: Boolean = true,
    val isShowAppBar: Boolean = true,
    val isCreate: Boolean = true,
    val isLoadingData: Boolean = false,
)
