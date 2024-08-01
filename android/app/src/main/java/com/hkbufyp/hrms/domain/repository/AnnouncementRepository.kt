package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.announcement.CreateAnnouncementPayload
import com.hkbufyp.hrms.data.remote.dto.announcement.UpdateAnnouncementPayload
import com.hkbufyp.hrms.domain.model.Announcement
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getAnnouncements(): Flow<NetworkResponse<List<Announcement>>>
    fun getAnnouncements(limit: Int): Flow<NetworkResponse<List<Announcement>>>
    fun getAnnouncement(id: Int): Flow<NetworkResponse<Announcement>>
    fun createAnnouncement(payload: CreateAnnouncementPayload): Flow<NetworkResponse<Int>>
    fun updateAnnouncement(id: Int, payload: UpdateAnnouncementPayload): Flow<NetworkResponse<String>>
    fun removeAnnouncement(id: Int): Flow<NetworkResponse<String>>
}