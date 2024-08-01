package com.hkbufyp.hrms.ui.screen.management.announcement.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAnnounceScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    postAnnounceViewModel: PostAnnounceViewModel,
    onNavigationUpClicked: () -> Unit,
    onPublished: (Int) -> Unit
) {

    val uiState by postAnnounceViewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Publish Announcement")
        sharedViewModel.isShowAppBar(false)

        launch {
            postAnnounceViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            postAnnounceViewModel.announcementId.collectLatest {
                if (it > 0) {
                    sharedViewModel.isShowAppBar(true)
                    onPublished(it)
                }
            }
        }

        launch {
            postAnnounceViewModel.focusRequest.collectLatest {
                if (it) {
                    focusRequester.requestFocus()
                }
            }
        }
    }

    if (uiState.isShowAlertBox) {
        AlertDialog(
            onDismissRequest = {
               postAnnounceViewModel.onEvent(PostAnnounceEvent.DismissDialog)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        postAnnounceViewModel.onEvent(PostAnnounceEvent.Publish)
                    }
                ) {
                    Text(text = "Publish")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        postAnnounceViewModel.onEvent(PostAnnounceEvent.DismissDialog)
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Announcement,
                    contentDescription = "Announcement"
                )
            },
            text = {
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Are you sure to publish the announcement?")
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.isPushNotification,
                            onCheckedChange = {
                                postAnnounceViewModel.onEvent(PostAnnounceEvent.CheckStateChanged(it))
                            }
                        )
                        Text(text = "Push Notification")
                    }
                }
            }
        )
    }

    Scaffold (
        topBar = {
            if (uiState.isShowAppBar) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Publish Announcement") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { onNavigationUpClicked() }
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
                                postAnnounceViewModel.onEvent(PostAnnounceEvent.Submit)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Publish"
                            )
                        }
                    }
                )
            }
        }
    ) {
        if (uiState.isLoadingData) {
            LoadingIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it)
            ) {
                if (uiState.isSubmitting) {
                    FullScreenLoading()
                }

                Column {
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = { title ->
                            postAnnounceViewModel.onEvent(PostAnnounceEvent.TitleChanged(title))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Title",
                                color = Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )

                    HorizontalDivider()

                    OutlinedTextField(
                        value = uiState.content,
                        onValueChange = { content ->
                            postAnnounceViewModel.onEvent(PostAnnounceEvent.ContentChanged(content))
                        },
                        modifier = Modifier.fillMaxSize(),
                        placeholder = {
                            Text(
                                text = "Content",
                                modifier = Modifier.fillMaxSize(),
                                color = Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )
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