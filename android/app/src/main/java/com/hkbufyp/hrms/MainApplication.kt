package com.hkbufyp.hrms

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.hkbufyp.hrms.di.appModule
import com.hkbufyp.hrms.domain.constant.NotificationConstant
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }

        createNotificationChannel()
        subscribeAnnouncement()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationConstant.NOTIFICATION_CHANNEL_ID,
            NotificationConstant.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun subscribeAnnouncement() {
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationConstant.TOPIC_ANNOUNCEMENT)
    }
}