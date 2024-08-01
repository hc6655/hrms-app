package com.hkbufyp.hrms.ui.screen.management.employee.employees

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hkbufyp.hrms.domain.model.User
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator

@Composable
fun ContactItem(
    data: User,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface (
                shape = CircleShape,
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.secondary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = data.lastName.first().toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }

            Text(
                text = data.lastName + " " + data.firstName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeesScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    employeesViewModel: EmployeesViewModel,
    onEmployeeClicked: (id: String) -> Unit,
    onNavigationUpClicked: () -> Unit
) {
    val uiState by employeesViewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)
    }

    LaunchedEffect(uiState.isSearching) {
        if (uiState.isSearching) {
            focusRequester.requestFocus()
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        Scaffold (
            topBar = {
                if (uiState.isShowAppbar) {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedVisibility(
                                visible = uiState.isSearching,
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { it }
                            ) {
                                TextField(
                                    value = uiState.searchText,
                                    onValueChange = {
                                        employeesViewModel.onEvent(EmployeesEvent.SearchTextChanged(it))
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color.Transparent,
                                        focusedIndicatorColor = Color(0xff424549),
                                        unfocusedIndicatorColor = Color(0xff424549),
                                        disabledIndicatorColor = Color.Transparent,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                    ),
                                    singleLine = true,
                                    placeholder = {
                                        Text(text = "Employee Name")
                                    },
                                    modifier = Modifier.focusRequester(focusRequester),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        focusManager.clearFocus()
                                        employeesViewModel.onEvent(EmployeesEvent.PerformSearch)
                                    })
                                )
                            }

                            AnimatedVisibility(
                                visible = !uiState.isSearching,
                                enter = slideInHorizontally { -it },
                                exit = slideOutHorizontally { -it }
                            ) {
                                Text(text = "Manage Employees")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (uiState.isSearching) {
                                        employeesViewModel.onEvent(EmployeesEvent.TriggerSearch(false))
                                    } else {
                                        onNavigationUpClicked()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    if (uiState.isSearching) {
                                        focusManager.clearFocus()
                                        employeesViewModel.onEvent(EmployeesEvent.PerformSearch)
                                    } else {
                                        employeesViewModel.onEvent(EmployeesEvent.TriggerSearch(true))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    )
                }
            }
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.employeesGroup.forEach { it ->
                    val l = it.sortedBy { it.lastName }
                    Text(
                        text = l[0].department.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(224, 224, 224)
                        )
                    ) {
                        l.forEach { item ->
                            ContactItem(
                                data = item,
                                onClick = {
                                    onEmployeeClicked(item.id)
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EmployeesPreview() {
    val isSearching by remember { mutableStateOf(true) }
    var test by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = test,
                            onValueChange = { test = it },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xff424549),
                                unfocusedIndicatorColor = Color(0xff424549),
                                disabledIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(text = "Employee Name")
                            }
                        )
                    } else {
                        Text(text = "Manage Employees")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color.Gray)
                .fillMaxSize()
        ) {

        }
    }
}