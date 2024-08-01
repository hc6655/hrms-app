package com.hkbufyp.hrms.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hkbufyp.hrms.ui.screen.home.HomeScreen
import com.hkbufyp.hrms.ui.screen.home.HomeViewModel
import com.hkbufyp.hrms.ui.screen.login.LoginScreen
import com.hkbufyp.hrms.ui.screen.login.LoginViewModel
import com.hkbufyp.hrms.ui.screen.management.leave.applications.LeaveApplicationsScreen
import com.hkbufyp.hrms.ui.screen.management.leave.applications.LeaveApplicationsViewModel
import com.hkbufyp.hrms.ui.screen.management.leave.approval.LeaveApprovalScreen
import com.hkbufyp.hrms.ui.screen.management.leave.approval.LeaveApprovalViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.apply.LeaveApplyScreen
import com.hkbufyp.hrms.ui.screen.user.leave.apply.LeaveApplyViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.history.LeaveHistoryViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.balance.LeaveBalanceScreen
import com.hkbufyp.hrms.ui.screen.user.leave.balance.LeaveBalanceViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.detail.LeaveDetailScreen
import com.hkbufyp.hrms.ui.screen.user.leave.detail.LeaveDetailViewModel
import com.hkbufyp.hrms.ui.screen.user.leave.history.LeaveHistoryScreen
import com.hkbufyp.hrms.util.navigateWithPopup
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.screen.announcement.detail.AnnounceDetailScreen
import com.hkbufyp.hrms.ui.screen.announcement.detail.AnnounceDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.announcement.manage.ManageAnnouncementScreen
import com.hkbufyp.hrms.ui.screen.management.announcement.manage.ManageAnnouncementViewModel
import com.hkbufyp.hrms.ui.screen.management.announcement.post.PostAnnounceScreen
import com.hkbufyp.hrms.ui.screen.management.announcement.post.PostAnnounceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ClockInDevicesScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices.BleDeviceScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices.BleDeviceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan.BleScanningScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan.BleScanningViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices.WifiDeviceScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices.WifiDeviceViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan.WifiDevicesScanningScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan.WifiScanningViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.method.ClockInMethodScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.method.ClockInMethodViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail.AttendTimeslotDetailScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail.AttendTimeslotDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots.AttendTimeslotScreen
import com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots.AttendTimeslotViewModel
import com.hkbufyp.hrms.ui.screen.management.department.departments.DepartmentsScreen
import com.hkbufyp.hrms.ui.screen.management.department.departments.DepartmentsViewModel
import com.hkbufyp.hrms.ui.screen.management.department.detail.DepartmentDetailScreen
import com.hkbufyp.hrms.ui.screen.management.department.detail.DepartmentDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.attend_record.EmployeeAttendRecordScreen
import com.hkbufyp.hrms.ui.screen.management.employee.attend_record.EmployeeAttendRecordViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot.EmployeeTimeslotScreen
import com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot.EmployeeTimeslotViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.detail.EmployeeDetailScreen
import com.hkbufyp.hrms.ui.screen.management.employee.detail.EmployeeDetailViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.employees.EmployeesScreen
import com.hkbufyp.hrms.ui.screen.management.employee.employees.EmployeesViewModel
import com.hkbufyp.hrms.ui.screen.management.employee.registration.EmployeeRegScreen
import com.hkbufyp.hrms.ui.screen.management.employee.registration.EmployeeRegViewModel
import com.hkbufyp.hrms.ui.screen.management.log.attend.AttendLogScreen
import com.hkbufyp.hrms.ui.screen.management.log.attend.AttendLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.employee.ManageEmployeeLogScreen
import com.hkbufyp.hrms.ui.screen.management.log.employee.ManageEmployeeLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.leave.ManageLeaveLogScreen
import com.hkbufyp.hrms.ui.screen.management.log.leave.ManageLeaveLogViewModel
import com.hkbufyp.hrms.ui.screen.management.log.login.LoginLogScreen
import com.hkbufyp.hrms.ui.screen.management.log.login.LoginLogViewModel
import com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth.SensitiveAuthScreen
import com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth.SensitiveAuthViewModel
import com.hkbufyp.hrms.ui.screen.user.announcement.AnnouncementScreen
import com.hkbufyp.hrms.ui.screen.user.announcement.AnnouncementViewModel
import com.hkbufyp.hrms.ui.screen.user.clockin.ClockInScreen
import com.hkbufyp.hrms.ui.screen.user.clockin.ClockInViewModel
import com.hkbufyp.hrms.util.navigate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HRMSNavigation(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                loginViewModel = loginViewModel,
                onNavToHomeScreen = {
                    sharedViewModel.setEmployeeInfo(it)
                    navHostController.navigateWithPopup(Screen.Home.route, Screen.Login.route)
                }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = koinViewModel { parametersOf(sharedViewModel) }
            HomeScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                homeViewModel = homeViewModel,
                onQuickActionClicked = {
                    navHostController.navigate(it)
                },
                onAnnouncementClicked = {
                    navHostController.navigate(Screen.AnnouncementDetail.route.replace(
                        oldValue = "{id}",
                        newValue = it.toString()
                    ))
                }
            )
        }

        composable(Screen.EmployeeRegistration.route) {
            val employeeRegViewModel: EmployeeRegViewModel = koinViewModel()
            EmployeeRegScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeeRegViewModel = employeeRegViewModel
            )
        }

        composable(Screen.ApplyLeave.route) {
            val leaveApplyViewModel: LeaveApplyViewModel = koinViewModel {
                parametersOf(sharedViewModel.sharedUiState.value.employeeInfo?.employeeId)
            }

            LeaveApplyScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                leaveApplyViewModel = leaveApplyViewModel,
                onApplySucceed = { id ->
                    navHostController.navigate(Screen.MyLeaveApplicationDetail.route.replace(
                        oldValue = "{recordId}",
                        newValue = id.toString()
                    )) {
                        popUpTo(Screen.ApplyLeave.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.CheckLeaveBalance.route) {
            val leaveBalanceViewModel: LeaveBalanceViewModel = koinViewModel {
                parametersOf(sharedViewModel.sharedUiState.value.employeeInfo?.employeeId)
            }

            LeaveBalanceScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                leaveBalanceViewModel = leaveBalanceViewModel
            )
        }

        composable(Screen.LeaveManagement.route) {
            val leaveApplicationsViewModel: LeaveApplicationsViewModel = koinViewModel()
            LeaveApplicationsScreen(
                sharedViewModel = sharedViewModel,
                leaveApplicationsViewModel = leaveApplicationsViewModel,
                onClickedVacationDetails = {
                    navHostController.navigate(Screen.LeaveApplicationApproval.route.replace(
                        oldValue = "{recordId}",
                        newValue = it.toString()
                    ))
                }
            )
        }

        composable(Screen.LeaveApplicationApproval.route) { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getString("recordId")?.toInt() ?: 0
            val leaveApprovalViewModel: LeaveApprovalViewModel =
                koinViewModel { parametersOf(applicationId) }

            LeaveApprovalScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                leaveApprovalViewModel = leaveApprovalViewModel
            )
        }

        composable(Screen.MyLeaveApplications.route) {
            val leaveHistoryViewModel: LeaveHistoryViewModel = koinViewModel {
                parametersOf(sharedViewModel.sharedUiState.value.employeeInfo?.employeeId)
            }

            LeaveHistoryScreen(
                sharedViewModel = sharedViewModel,
                leaveHistoryViewModel = leaveHistoryViewModel,
                onClickedVacationDetails = {
                    navHostController.navigate(Screen.MyLeaveApplicationDetail.route.replace(
                        oldValue = "{recordId}",
                        newValue = it.toString()
                    ))
                }
            )
        }

        composable(Screen.MyLeaveApplicationDetail.route) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getString("recordId")
            val leaveDetailViewModel: LeaveDetailViewModel = koinViewModel { parametersOf(recordId?.toInt()) }

            LeaveDetailScreen(
                sharedViewModel = sharedViewModel,
                leaveDetailViewModel = leaveDetailViewModel
            )
        }

        composable(Screen.Departments.route) {
            val departmentsViewModel: DepartmentsViewModel = koinViewModel()

            DepartmentsScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                departmentsViewModel = departmentsViewModel,
                onEditOrCreateClicked = {
                    navHostController.navigate(Screen.DepartmentDetail.route.replace(
                        oldValue = "{departmentId}",
                        newValue = it ?: "null"
                    ))
                }
            )
        }

        composable(
            Screen.DepartmentDetail.route) { backStackEntry ->
            val departmentId = backStackEntry.arguments?.getString("departmentId")
            val departmentDetailViewModel: DepartmentDetailViewModel = koinViewModel { parametersOf(departmentId) }

            DepartmentDetailScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                departmentDetailViewModel = departmentDetailViewModel,
                onSubmitSucceed = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                },
                onNavigationClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.AnnouncementDetail.route) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            val announceDetailViewModel: AnnounceDetailViewModel = koinViewModel {  parametersOf(announcementId) }

            AnnounceDetailScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                announceDetailViewModel = announceDetailViewModel
            )
        }

        composable(Screen.Announcement.route) {
            val announcementViewModel: AnnouncementViewModel = koinViewModel()

            AnnouncementScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                announcementViewModel = announcementViewModel,
                onAnnouncementClicked = {
                    navHostController.navigate(Screen.AnnouncementDetail.route.replace(
                        oldValue = "{id}",
                        newValue = it.toString()
                    ))
                }
            )
        }

        composable(Screen.PostAnnouncement.route) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val postAnnounceViewModel: PostAnnounceViewModel = koinViewModel  {  parametersOf(announcementId) }

            PostAnnounceScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                postAnnounceViewModel = postAnnounceViewModel,
                onNavigationUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                },
                onPublished = { id ->
                    navHostController.navigate(Screen.AnnouncementDetail.route.replace(
                        oldValue = "{id}",
                        newValue = id.toString()
                    )) {
                        popUpTo(Screen.PostAnnouncement.route) {
                            inclusive = true
                        }
                    }
                    sharedViewModel.isShowAppBar(true)
                }
            )
        }

        composable(Screen.ManageAnnouncement.route) {
            val manageAnnouncementViewModel: ManageAnnouncementViewModel = koinViewModel()

            ManageAnnouncementScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                manageAnnouncementViewModel = manageAnnouncementViewModel,
                onEditClicked = {
                    navHostController.navigate(Screen.PostAnnouncement.route.replace(
                        oldValue = "{id}",
                        newValue = it.toString()
                    ))
                }
            )
        }

        composable(Screen.SensitiveAuth.route) { backStackEntry ->
            val originalRoute = backStackEntry.arguments?.getString("originalRoute")
            val sensitiveAuthViewModel: SensitiveAuthViewModel = koinViewModel {
                parametersOf(sharedViewModel.sharedUiState.value.employeeInfo?.employeeId ?: "")
            }

            SensitiveAuthScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                sensitiveAuthViewModel = sensitiveAuthViewModel,
                onAuthSucceed = {
                    navHostController.navigate(originalRoute ?: Screen.Home.route) {
                        popUpTo(Screen.SensitiveAuth.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.EmployeeManagement.route) {
            val employeesViewModel: EmployeesViewModel = koinViewModel()

            EmployeesScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeesViewModel = employeesViewModel,
                onEmployeeClicked = { id ->
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigate(Screen.EmployeeDetail.route.replace(
                        oldValue = "{employeeId}",
                        newValue = id
                    ))
                },
                onNavigationUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.EmployeeDetail.route) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId")
            val employeeDetailViewModel: EmployeeDetailViewModel = koinViewModel { parametersOf(employeeId) }

            EmployeeDetailScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeeDetailViewModel = employeeDetailViewModel
            )
        }

        composable(Screen.ClockInDevices.route) {
            ClockInDevicesScreen(
                sharedViewModel = sharedViewModel,
                onTypeClicked = {
                    when (it) {
                        "Wifi" -> {
                            navHostController.navigate(Screen.ClockInWifiDevices.route)
                        }
                        "Bluetooth" -> {
                            navHostController.navigate(Screen.ClockInBLEDevices.route)
                        }
                    }
                }
            )
        }

        composable(Screen.WifiScanning.route) {
            val wifiScanningViewModel: WifiScanningViewModel = koinViewModel()

            WifiDevicesScanningScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                wifiScanningViewModel = wifiScanningViewModel,
                onNavigationUpClicked = {
                    navHostController.navigateUp()
                },
                onAddSucceeded = {
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.ClockInWifiDevices.route) {
            val wifiDeviceViewModel: WifiDeviceViewModel = koinViewModel()

            WifiDeviceScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                wifiDeviceViewModel = wifiDeviceViewModel,
                onAddDeviceClicked = {
                    navHostController.navigate(Screen.WifiScanning.route)
                },
                onNavigateUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.ClockIn.route) {
            val employeeId = sharedViewModel.sharedUiState.value.employeeInfo?.employeeId ?: ""
            val clockInViewModel: ClockInViewModel = koinViewModel { parametersOf(employeeId) }

            ClockInScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                clockInViewModel = clockInViewModel
            )
        }

        composable(Screen.AttendTimeslotDetail.route) { backStackEntry ->
            val slotId = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            val attendTimeslotDetailViewModel: AttendTimeslotDetailViewModel = koinViewModel { parametersOf(slotId) }

            AttendTimeslotDetailScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                attendTimeslotDetailViewModel = attendTimeslotDetailViewModel,
                onSubmitSucceeded = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                },
                onNavigateBack = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.AttendTimeslot.route) {
            val attendTimeslotViewModel: AttendTimeslotViewModel = koinViewModel()

            AttendTimeslotScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                attendTimeslotViewModel = attendTimeslotViewModel,
                onAddClicked = {
                    navHostController.navigate(Screen.AttendTimeslotDetail.route.replace(
                        oldValue = "{id}",
                        newValue = "0"
                    ))
                },
                onSlotClicked = { id ->
                    navHostController.navigate(Screen.AttendTimeslotDetail.route.replace(
                        oldValue = "{id}",
                        newValue = id.toString()
                    ))
                } ,
                onNavigateBackClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.SetCustomEmployeeTimeslot.route) {
            val employeesViewModel: EmployeesViewModel = koinViewModel()

            EmployeesScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeesViewModel = employeesViewModel,
                onEmployeeClicked = { id ->
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigate(Screen.CustomEmployeeTimeslot.route.replace(
                        oldValue = "{id}",
                        newValue = id
                    ))
                },
                onNavigationUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.CustomEmployeeTimeslot.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val employeeTimeslotViewModel: EmployeeTimeslotViewModel = koinViewModel { parametersOf(id) }

            EmployeeTimeslotScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeeTimeslotViewModel = employeeTimeslotViewModel
            )
        }

        composable(Screen.BleScanning.route) {
            val bleScanningViewModel: BleScanningViewModel = koinViewModel()

            BleScanningScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                bleScanningViewModel = bleScanningViewModel,
                onNavigateUpClicked = {
                    navHostController.navigateUp()
                },
                onAddSucceeded = {
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.ClockInBLEDevices.route) {
            val bleDeviceViewModel: BleDeviceViewModel = koinViewModel()

            BleDeviceScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                bleDeviceViewModel = bleDeviceViewModel,
                onAddDeviceClicked = {
                    navHostController.navigate(Screen.BleScanning.route)
                },
                onNavigateUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.LoginLog.route) {
            val loginLogViewModel: LoginLogViewModel = koinViewModel()

            LoginLogScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                loginLogViewModel = loginLogViewModel,
                onNavigationClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.AttendLog.route) {
            val attendLogViewModel: AttendLogViewModel = koinViewModel()

            AttendLogScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                attendLogViewModel = attendLogViewModel,
                onNavigationClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.EmployeeManageLog.route) {
            val manageEmployeeLogViewModel: ManageEmployeeLogViewModel = koinViewModel()

            ManageEmployeeLogScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                manageEmployeeLogViewModel = manageEmployeeLogViewModel,
                onNavigationClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.LeaveManageLog.route) {
            val manageLeaveLogViewModel: ManageLeaveLogViewModel = koinViewModel()

            ManageLeaveLogScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                manageLeaveLogViewModel = manageLeaveLogViewModel,
                onNavigationClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.EmployeeAttendRecord.route) {
            val employeesViewModel: EmployeesViewModel = koinViewModel()

            EmployeesScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeesViewModel = employeesViewModel,
                onEmployeeClicked = { id ->
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigate(Screen.EmployeeAttendRecordDetail.route.replace(
                        oldValue = "{id}",
                        newValue = id
                    ))
                },
                onNavigationUpClicked = {
                    sharedViewModel.isShowAppBar(true)
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.EmployeeAttendRecordDetail.route) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("id")
            val employeeAttendRecordViewModel: EmployeeAttendRecordViewModel = koinViewModel { parametersOf(employeeId) }

            EmployeeAttendRecordScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                employeeAttendRecordViewModel = employeeAttendRecordViewModel
            )
        }

        composable(Screen.ClockInMethod.route) {
            val clockInMethodViewModel: ClockInMethodViewModel = koinViewModel()

            ClockInMethodScreen(
                snackbarHostState = snackbarHostState,
                sharedViewModel = sharedViewModel,
                clockInMethodViewModel = clockInMethodViewModel
            )
        }
    }
}