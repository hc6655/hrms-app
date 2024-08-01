package com.hkbufyp.hrms.ui.screen.user.announcement

sealed class AnnouncementEvent {
    data object Refresh: AnnouncementEvent()
}
