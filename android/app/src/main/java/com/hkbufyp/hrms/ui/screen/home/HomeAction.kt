package com.hkbufyp.hrms.ui.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Airlines
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PunchClock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.hkbufyp.hrms.ui.navigation.Screen

sealed class ActionNav(
    val name: String,
    val icon: ImageVector,
    val screen: Screen
) {
    data object Home: ActionNav("Home", Icons.Filled.Home, Screen.Home)
    data object Settings: ActionNav("Settings", Icons.Filled.Settings, Screen.Setting)
    data object EmployeeRegistration: ActionNav("Registration", Icons.Filled.PersonAdd, Screen.EmployeeRegistration)
    data object EmployeeManagement: ActionNav("Manage All", Icons.Filled.ManageAccounts, Screen.EmployeeManagement)
    data object Announcement: ActionNav("Announcement", Icons.Filled.Announcement, Screen.Announcement)
    data object ClockIn: ActionNav("Clock In / Out", Icons.Filled.PunchClock, Screen.ClockIn)
    data object ApplyLeave: ActionNav("Apply Leave", Icons.Filled.DateRange, Screen.ApplyLeave)
    data object CheckLeave: ActionNav("Leave Balance", Icons.Filled.DateRange, Screen.CheckLeaveBalance)
    data object MyLeaveRecords: ActionNav("My Leave Records", Icons.Filled.Approval, Screen.MyLeaveApplications)
    data object LeaveApproval: ActionNav("Leave Approval", Icons.Filled.Approval, Screen.LeaveManagement)
    data object Departments: ActionNav("Departments", Icons.Filled.AccountTree, Screen.Departments)
    data object PostAnnouncement: ActionNav("Publish Announcement", Icons.Filled.NotificationAdd, Screen.PostAnnouncement)
    data object ClockInDevices: ActionNav("Clock-in Devices", Icons.Filled.Devices, Screen.ClockInDevices)
    data object AttendTimeslot: ActionNav("Shift Timing", Icons.Filled.AccessTime, Screen.AttendTimeslot)
    data object CustomEmployeeTimeslot: ActionNav("Custom shift timing", Icons.Filled.DashboardCustomize, Screen.SetCustomEmployeeTimeslot)
    data object ManageAnnouncement: ActionNav("Manage Announcement", Icons.Filled.Announcement, Screen.ManageAnnouncement)
    data object LoginLog: ActionNav("Login Log", Icons.Filled.Login, Screen.LoginLog)
    data object AttendLog: ActionNav("Attend Log", Icons.Filled.PunchClock, Screen.AttendLog)
    data object EmployeeManageLog: ActionNav("Manage Employee Log", Icons.Filled.AccountCircle, Screen.EmployeeManageLog)
    data object LeaveManageLog: ActionNav("Manage Leave Log", Icons.Filled.AirplaneTicket, Screen.LeaveManageLog)
    data object EmployeeAttendRecord: ActionNav("Attendance Record", Icons.Filled.PunchClock, Screen.EmployeeAttendRecord)
    data object ClockInMethod: ActionNav("Clock-in Method", Icons.Filled.DeviceHub, Screen.ClockInMethod)

    companion object {
        val navigationMainActions = listOf(Home, Settings, Announcement)
        val homeQuickActions = listOf(ClockIn, CheckLeave, Announcement)
    }
}

sealed class SideBarActionNav(
    val name: String,
    val icon: ImageVector,
    val actions: List<ActionNav>,
    val requireManagement: Boolean = false,
    val requireAccessLog: Boolean = false,
) {
    var isExpand by mutableStateOf(false)

    data object Department: SideBarActionNav("Department", Icons.Filled.AccountTree, listOf(
        ActionNav.Departments
    ), requireManagement = true)

    data object Announcement: SideBarActionNav("Announcement", Icons.Filled.Notifications, listOf(
        ActionNav.PostAnnouncement,
        ActionNav.ManageAnnouncement
    ), requireManagement = true)

    data object Leave: SideBarActionNav("Leave", Icons.Filled.AirplaneTicket, listOf(
        ActionNav.ApplyLeave,
        ActionNav.CheckLeave,
        ActionNav.MyLeaveRecords
    ))

    data object Employee: SideBarActionNav("Manage Employee", Icons.Filled.ManageAccounts, listOf(
        ActionNav.EmployeeRegistration,
        ActionNav.EmployeeManagement,
        ActionNav.CustomEmployeeTimeslot,
        ActionNav.EmployeeAttendRecord
    ), requireManagement = true)

    data object ManageVacation: SideBarActionNav("Manage Leave", Icons.Filled.Airlines, listOf(
        ActionNav.LeaveApproval
    ), requireManagement = true)

    data object ManageClockIn: SideBarActionNav("Manage Clock-In", Icons.Filled.PunchClock, listOf(
        ActionNav.ClockInDevices,
        ActionNav.AttendTimeslot,
        ActionNav.ClockInMethod
    ), requireManagement = true)

    data object ClockIn: SideBarActionNav("Clock-In", Icons.Filled.PunchClock, listOf(
        ActionNav.ClockIn
    ))

    data object Log: SideBarActionNav("Log", Icons.Filled.FileCopy, listOf(
        ActionNav.LoginLog,
        ActionNav.AttendLog,
        ActionNav.EmployeeManageLog,
        ActionNav.LeaveManageLog
    ), requireManagement = true, requireAccessLog = true)



    companion object {
        private val managementActions = listOf(Department, Announcement, Employee, ManageVacation, ManageClockIn, Log)
        private val userActions = listOf(ClockIn, Leave)
        val actions = listOf(userActions, null, managementActions)

        fun shrinkAll() {
            actions.forEach { action ->
                action?.forEach { nav ->
                    nav.isExpand = false
                }
            }
        }
    }
}