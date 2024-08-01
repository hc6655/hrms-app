package com.hkbufyp.hrms.ui.screen.management.announcement.manage

import com.hkbufyp.hrms.domain.model.Announcement

sealed class ManageAnnouncementEvent {
    data object Enter: ManageAnnouncementEvent()
    data object Refresh: ManageAnnouncementEvent()
    data class SelectRemove(val obj: Announcement): ManageAnnouncementEvent()
    data object CancelRemove: ManageAnnouncementEvent()
    data object Remove: ManageAnnouncementEvent()
}
