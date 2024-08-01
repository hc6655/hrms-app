package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.AnnouncementService
import com.hkbufyp.hrms.data.remote.dto.announcement.AnnouncementDto
import com.hkbufyp.hrms.data.remote.dto.announcement.AnnouncementsResponse
import com.hkbufyp.hrms.data.remote.dto.announcement.CreateAnnouncementPayload
import com.hkbufyp.hrms.data.remote.dto.announcement.CreateAnnouncementResponse
import com.hkbufyp.hrms.data.remote.dto.announcement.UpdateAnnouncementPayload
import com.hkbufyp.hrms.data.remote.dto.announcement.toAnnouncement
import com.hkbufyp.hrms.domain.model.Announcement
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnnouncementRepositoryImpl(
    private val announcementService: AnnouncementService
): AnnouncementRepository {

    override fun getAnnouncements() =
        flow {
            val response = announcementService.getAnnouncements()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val announceResponse = response.body<AnnouncementsResponse>()
                    val announcements = announceResponse.announcements

                    emit(NetworkResponse.Success(announcements.map { it.toAnnouncement() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getAnnouncements(limit: Int) =
        flow {
            val response = announcementService.getAnnouncements(limit = limit)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val announceResponse = response.body<AnnouncementsResponse>()
                    val announcements = announceResponse.announcements

                    emit(NetworkResponse.Success(announcements.map { it.toAnnouncement() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getAnnouncement(id: Int) =
        flow {
            val response = announcementService.getAnnouncement(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val dto = response.body<AnnouncementDto>()
                    emit(NetworkResponse.Success(dto.toAnnouncement()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun createAnnouncement(payload: CreateAnnouncementPayload) =
        flow {
            val response = announcementService.createAnnouncement(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val res = response.body<CreateAnnouncementResponse>()
                    emit(NetworkResponse.Success(res.announcementId.toInt()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateAnnouncement(id: Int, payload: UpdateAnnouncementPayload) =
        flow {
            val response = announcementService.updateAnnouncement(id, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun removeAnnouncement(id: Int) =
        flow {
            val response = announcementService.removeAnnouncement(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}