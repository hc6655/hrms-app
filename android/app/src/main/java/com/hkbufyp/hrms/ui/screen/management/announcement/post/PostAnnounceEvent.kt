package com.hkbufyp.hrms.ui.screen.management.announcement.post

sealed class PostAnnounceEvent {
    data object Submit: PostAnnounceEvent()
    data object Publish: PostAnnounceEvent()
    data object DismissDialog: PostAnnounceEvent()
    data class TitleChanged(val title: String): PostAnnounceEvent()
    data class ContentChanged(val content: String): PostAnnounceEvent()
    data class CheckStateChanged(val checked: Boolean): PostAnnounceEvent()
}
