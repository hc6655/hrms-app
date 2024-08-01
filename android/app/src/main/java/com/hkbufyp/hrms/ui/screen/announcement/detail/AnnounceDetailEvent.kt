package com.hkbufyp.hrms.ui.screen.announcement.detail

sealed class AnnounceDetailEvent {
    data object Refresh: AnnounceDetailEvent()
}
