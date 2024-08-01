package com.hkbufyp.hrms.ui.screen.announcement

sealed class AnnounceEvent {
    data object Enter: AnnounceEvent()
    data object Refresh: AnnounceEvent()
}
