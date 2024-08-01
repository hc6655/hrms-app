package com.hkbufyp.hrms.ui.screen.management.department.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hkbufyp.hrms.R
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.components.LoadingIndicatorForButton
import com.hkbufyp.hrms.ui.components.ToggleField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentDetailScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    departmentDetailViewModel: DepartmentDetailViewModel,
    onSubmitSucceed: () -> Unit,
    onNavigationClicked: () -> Unit
) {

    val uiState by departmentDetailViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)

        launch {
            departmentDetailViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            departmentDetailViewModel.isSubmitSucceed.collectLatest {
                if (it) {
                    onSubmitSucceed()
                }
            }
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = if (uiState.isCreation) "Create Department" else "Edit Department")
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigationClicked() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            departmentDetailViewModel.onEvent(DepartmentDetailEvent.Submit)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Submit")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it),
        ) {
            if (uiState.isLoadingData) {
                LoadingIndicator()
            } else {
                FullScreenLoading(show = uiState.isSubmitting)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Department ID",
                        fontSize = 20.sp
                    )
                    OutlinedTextField(
                        value = uiState.department?.id ?: "",
                        onValueChange = {
                            departmentDetailViewModel.onEvent(DepartmentDetailEvent.IdChanged(it))
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Department ID") },
                        enabled = uiState.isCreation
                    )
                    Text(
                        text = "Department Title",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    OutlinedTextField(
                        value = uiState.department?.title ?: "",
                        onValueChange = {
                            departmentDetailViewModel.onEvent(DepartmentDetailEvent.TitleChanged(it))
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Department Title") }
                    )

                    HorizontalDivider(modifier = Modifier.padding(top = 15.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Position Setting",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(top = 12.dp)
                        ) {

                            uiState.department!!.positions.forEachIndexed { index, position ->
                                var isExpanded by remember { mutableStateOf(false) }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column (
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            BasicTextField(
                                                value = position.name,
                                                onValueChange = {
                                                    departmentDetailViewModel.onEvent(DepartmentDetailEvent.SetPositionName(index, it))
                                                },
                                                modifier = Modifier.height(35.dp),
                                                singleLine = true,
                                                interactionSource = remember { MutableInteractionSource() },
                                            ) {
                                                OutlinedTextFieldDefaults.DecorationBox(
                                                    value = position.name,
                                                    innerTextField = it,
                                                    enabled = true,
                                                    singleLine = true,
                                                    visualTransformation = VisualTransformation.None,
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    contentPadding = OutlinedTextFieldDefaults.contentPadding(
                                                        top = 0.dp,
                                                        bottom = 0.dp
                                                    ),
                                                    placeholder = {
                                                        Text(text = "Position title")
                                                    }
                                                )
                                            }

                                            ExposedDropdownMenuBox(
                                                expanded = isExpanded,
                                                onExpandedChange = { expand ->
                                                    isExpanded = expand
                                                }
                                            ) {
                                                BasicTextField(
                                                    value = uiState.accessLevelList.getOrNull(position.accessLevel) ?: "Access Level",
                                                    onValueChange = { },
                                                    modifier = Modifier
                                                        .height(35.dp)
                                                        .menuAnchor(),
                                                    singleLine = true,
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    readOnly = true,
                                                ) {
                                                    OutlinedTextFieldDefaults.DecorationBox(
                                                        value = uiState.accessLevelList.getOrNull(position.accessLevel) ?: "Access Level",
                                                        innerTextField = it,
                                                        enabled = true,
                                                        singleLine = true,
                                                        visualTransformation = VisualTransformation.None,
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        contentPadding = OutlinedTextFieldDefaults.contentPadding(
                                                            top = 0.dp,
                                                            bottom = 0.dp
                                                        ),
                                                        trailingIcon = {
                                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                                        },
                                                    )
                                                }

                                                ExposedDropdownMenu(
                                                    expanded = isExpanded,
                                                    onDismissRequest = {
                                                        isExpanded = false
                                                    },
                                                    modifier = Modifier.background(color = Color.White),
                                                    content = {
                                                        uiState.accessLevelList.forEachIndexed { level, s ->
                                                            DropdownMenuItem(
                                                                text = { Text(text = s) },
                                                                onClick = {
                                                                    departmentDetailViewModel.onEvent(DepartmentDetailEvent.SetAccessLevel(index, level))
                                                                    isExpanded = false
                                                                }
                                                            )
                                                        }
                                                    }
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    departmentDetailViewModel.onEvent(DepartmentDetailEvent.RemovePosition(index))
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Delete"
                                                )
                                            }
                                        }

                                        ToggleField(
                                            desc = "Management Feature",
                                            checked = position.managementFeature,
                                            onCheckedChange = {
                                                departmentDetailViewModel.onEvent(DepartmentDetailEvent.SetManagement(index, it))
                                            }
                                        )

                                        ToggleField(
                                            desc = "Access Log",
                                            checked = position.accessLog,
                                            onCheckedChange = {
                                                departmentDetailViewModel.onEvent(DepartmentDetailEvent.SetAccessLog(index, it))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    departmentDetailViewModel.onEvent(DepartmentDetailEvent.AddPosition)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
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
fun DefaultPreview() {

}