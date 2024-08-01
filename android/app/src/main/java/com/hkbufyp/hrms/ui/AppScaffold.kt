package com.hkbufyp.hrms.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hkbufyp.hrms.ui.navigation.HRMSNavigation
import com.hkbufyp.hrms.ui.navigation.Screen
import com.hkbufyp.hrms.ui.screen.home.ActionNav
import com.hkbufyp.hrms.ui.screen.home.SideBarActionNav
import com.hkbufyp.hrms.util.navigateWithPopup
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.util.navigate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val sharedUiState by sharedViewModel.sharedUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed,)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = sharedUiState.isEnableSwipeSideBar,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = MaterialTheme.shapes.small,
                drawerContainerColor = Color.White,
                drawerContentColor = Color.Black,
                drawerTonalElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Image(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "",
                            modifier = Modifier
                                .size(70.dp)
                                .padding(start = 20.dp)
                        )

                        Text(
                            text = sharedUiState.employeeInfo?.employeeName ?: "",
                            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
                            fontSize = 15.sp
                        )

                        Divider(modifier = Modifier.padding(bottom = 10.dp))

                        ActionNav.navigationMainActions.forEach {
                            NavigationDrawerItem(
                                label = { Text(text = it.name) },
                                selected = false,
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.close()
                                        navHostController.navigate(it.screen)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = "",
                                        modifier = Modifier.padding(start = 10.dp))
                                       },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = Color.Gray,
                                    unselectedTextColor = Color.Black,
                                    selectedTextColor = Color.Black,
                                    unselectedBadgeColor = Color.White,
                                    selectedBadgeColor = Color.White
                                ),
                                shape = RectangleShape
                            )
                        }

                        Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))

                        SideBarActionNav.actions.forEach { navList ->
                            if (navList == null) {
                                Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))
                            } else {
                                navList.forEach inner@{ nav ->
                                    if (nav.requireManagement && sharedUiState.jwt?.managementFeature == false) {
                                        return@inner
                                    }

                                    if (nav.requireAccessLog && sharedUiState.jwt?.accessLog == false) {
                                        return@inner
                                    }

                                    NavigationDrawerItem(
                                        label = { Text(text = nav.name) },
                                        selected = false,
                                        onClick = { nav.isExpand = !nav.isExpand },
                                        icon = { Icon(imageVector = nav.icon, contentDescription = "") },
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .fillMaxWidth()
                                            .height(40.dp),
                                        colors = NavigationDrawerItemDefaults.colors(
                                            unselectedContainerColor = Color.Transparent,
                                            selectedContainerColor = Color.Gray,
                                            unselectedTextColor = Color.Black,
                                            selectedTextColor = Color.Black,
                                            unselectedBadgeColor = Color.White,
                                            selectedBadgeColor = Color.White
                                        ),
                                        badge = {
                                            Badge(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.Black
                                            ) {
                                                if (nav.isExpand) {
                                                    Icon(imageVector = Icons.Filled.ArrowDropUp, contentDescription = "")
                                                } else {
                                                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                                                }

                                            }
                                        }
                                    )
                                    AnimatedVisibility(visible = nav.isExpand) {
                                        Column (
                                            modifier = Modifier.padding(start = 30.dp)
                                        ) {
                                            nav.actions.forEach {
                                                NavigationDrawerItem(
                                                    label = { Text(text = it.name) },
                                                    selected = false,
                                                    onClick = {
                                                        navHostController.navigate(it.screen)
                                                        coroutineScope.launch {
                                                            drawerState.close()
                                                        }
                                                    },
                                                    icon = { Icon(imageVector = it.icon, contentDescription = "") },
                                                    modifier = Modifier
                                                        .padding(start = 10.dp)
                                                        .fillMaxWidth()
                                                        .height(40.dp),
                                                    colors = NavigationDrawerItemDefaults.colors(
                                                        unselectedContainerColor = Color.Transparent,
                                                        selectedContainerColor = Color.Gray,
                                                        unselectedTextColor = Color.Black,
                                                        selectedTextColor = Color.Black,
                                                        unselectedBadgeColor = Color.White,
                                                        selectedBadgeColor = Color.White
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    if (sharedUiState.isShowTopAppBar) {
                        CenterAlignedTopAppBar(
                            title = { Text(sharedUiState.appBarTitle) },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.White
                            ),
                            navigationIcon = {
                                if (navHostController.previousBackStackEntry != null) {
                                    IconButton(
                                        onClick = { navHostController.navigateUp() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        sharedViewModel.resetData()
                                        navHostController.navigateWithPopup(Screen.Login.route, Screen.Home.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ExitToApp,
                                        contentDescription = "Logout"
                                    )
                                }
                            }
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(it).background(Color.White)
                ) {
                    HRMSNavigation(navHostController, snackbarHostState, sharedViewModel)
                }
            }
        }
    )
}