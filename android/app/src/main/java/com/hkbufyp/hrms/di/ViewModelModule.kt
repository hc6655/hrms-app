package com.hkbufyp.hrms.di

import com.hkbufyp.hrms.ui.screen.home.HomeViewModel
import com.hkbufyp.hrms.ui.screen.login.LoginViewModel
import com.hkbufyp.hrms.ui.screen.management.leave.applications.LeaveApplicationsViewModel
import com.hkbufyp.hrms.ui.screen.management.leave.approval.LeaveApprovalViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.apply.LeaveApplyViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.history.LeaveHistoryViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.balance.LeaveBalanceViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.detail.LeaveDetailViewModel
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.screen.announcement.detail.AnnounceDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.announcement.manage.ManageAnnouncementViewModel
import com.hkbufyp.hrms.ui.screen.management.announcement.post.PostAnnounceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices.BleDeviceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan.BleScanningViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices.WifiDeviceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan.WifiScanningViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.method.ClockInMethodViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail.AttendTimeslotDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots.AttendTimeslotViewModel
import com.hkbufyp.hrms.ui.screen.management.department.departments.DepartmentsViewModel
import com.hkbufyp.hrms.ui.screen.management.department.detail.DepartmentDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.attend_record.EmployeeAttendRecordViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot.EmployeeTimeslotViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.detail.EmployeeDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.employees.EmployeesViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.registration.EmployeeRegViewModel
import com.hkbufyp.hrms.ui.screen.management.log.attend.AttendLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.employee.ManageEmployeeLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.leave.ManageLeaveLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.login.LoginLogViewModel
import com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth.SensitiveAuthViewModel
import com.hkbufyp.hrms.ui.screen.user.announcement.AnnouncementViewModel
import com.hkbufyp.hrms.ui.screen.user.clockin.ClockInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SharedViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { parameters -> HomeViewModel(sharedViewModel = parameters.get(), get(), get(), get()) }
    viewModel { LeaveBalanceViewModel(get(), get()) }
    viewModel { LeaveHistoryViewModel(get(), get()) }
    viewModel { LeaveDetailViewModel(get(), get()) }
    viewModel { LeaveApplyViewModel(get(), get()) }
    viewModel { LeaveApplicationsViewModel(get()) }
    viewModel { LeaveApprovalViewModel(get(), get()) }
    viewModel { EmployeeRegViewModel(get(), get(), get(), get()) }
    viewModel { DepartmentsViewModel(get()) }
    viewModel { DepartmentDetailViewModel(get(), get()) }
    viewModel { PostAnnounceViewModel(get(), get()) }
    viewModel { AnnounceDetailViewModel(get(), get()) }
    viewModel { SensitiveAuthViewModel(get(), get()) }
    viewModel { EmployeesViewModel(get()) }
    viewModel { EmployeeDetailViewModel(get(), get(), get(), get(), get()) }
    viewModel { WifiScanningViewModel(get()) }
    viewModel { WifiDeviceViewModel(get()) }
    viewModel { ClockInViewModel(get(), get(), get(), get(), get()) }
    viewModel { AttendTimeslotDetailViewModel(get(), get()) }
    viewModel { AttendTimeslotViewModel(get()) }
    viewModel { BleScanningViewModel(get()) }
    viewModel { BleDeviceViewModel(get()) }
    viewModel { AnnouncementViewModel(get()) }
    viewModel { ManageAnnouncementViewModel(get()) }
    viewModel { LoginLogViewModel(get()) }
    viewModel { ManageEmployeeLogViewModel(get()) }
    viewModel { ManageLeaveLogViewModel(get()) }
    viewModel { ClockInMethodViewModel(get()) }
    viewModel { EmployeeAttendRecordViewModel(get(), get(), get()) }
    viewModel { AttendLogViewModel(get()) }
    viewModel { EmployeeTimeslotViewModel(get(), get()) }
}