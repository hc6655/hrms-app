package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.announcement.CreateAnnouncementPayload
import com.hkbufyp.hrms.data.remote.dto.announcement.UpdateAnnouncementPayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class AnnouncementService(private val ktor: Ktor) {
    suspend fun getAnnouncements() =
        ktor.client.get {
            url(ApiService.Announcement.root)
        }

    suspend fun getAnnouncements(limit: Int) =
        ktor.client.get {
            url(ApiService.Announcement.root)
            parameter("limit", limit)
        }

    suspend fun getAnnouncement(id: Int) =
        ktor.client.get {
            url("${ApiService.Announcement.root}/$id")
        }

    suspend fun createAnnouncement(payload: CreateAnnouncementPayload) =
        ktor.client.post {
            url(ApiService.Announcement.root)
            setBody(payload)
        }

    suspend fun updateAnnouncement(id: Int, payload: UpdateAnnouncementPayload) =
        ktor.client.put {
            url("${ApiService.Announcement.root}/$id")
            setBody(payload)
        }

    suspend fun removeAnnouncement(id: Int) =
        ktor.client.delete {
            url("${ApiService.Announcement.root}/$id")
        }
}