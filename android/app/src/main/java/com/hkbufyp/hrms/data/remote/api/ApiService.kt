package com.hkbufyp.hrms.data.remote.api

object ApiService {
    private val baseUrl = "http://192.168.50.204:14753/api"

    object User {
        val root = "$baseUrl/user"
        val login = "$root/login"
        val getPermission = "$root/permission"
        val auth = "$root/auth"
    }

    object Role {
        val root = "$baseUrl/position"
    }

    object Department {
        val root = "$baseUrl/department"
    }

    object Leave {
        val root = "$baseUrl/leave"
        val userBalance = "$root/user"
        val applications = "$root/applications"
        val applicationsByUser = "$applications/user"
    }

    object FCM {
        val root = "$baseUrl/fcm"
    }

    object Announcement {
        val root = "$baseUrl/announcement"
    }

    object Wifi {
        val root = "$baseUrl/wifi"
    }

    object Ble {
        val root = "$baseUrl/ble"
    }

    object Attend {
        val root = "$baseUrl/attend"
        val timeslot = "$root/timeslot"
        val time = "$root/time"
        val method = "$root/method"
        val custom = "$timeslot/custom"
    }

    object Log {
        val root = "$baseUrl/log"
        val login = "$root/login"
        val employee = "$root/memployee"
        val leave = "$root/mleave"
        val attend = "$root/attend"
    }
}