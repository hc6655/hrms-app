package com.hkbufyp.hrms.ui.screen.user.announcement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.domain.model.Announcement
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnnouncementScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    announcementViewModel: AnnouncementViewModel,
    onAnnouncementClicked: (Int) -> Unit
) {

    val uiState by announcementViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Announcement")

        announcementViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = uiState.isRefreshing
        ),
        onRefresh = {
            announcementViewModel.onEvent(AnnouncementEvent.Refresh)
        }
    ) {
        if (uiState.isLoadingData) {
            LoadingIndicator()
        } else if (uiState.announcements?.isEmpty() == true) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Announcement")
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.announcements!!) { announce ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable {
                                    onAnnouncementClicked(announce.id)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = announce.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }

}

@Composable
@Preview
fun AnnouncementPreview() {
    val list = listOf(
        Announcement(
            id = 0,
            title = "Test Announcement",
            content = "Test Announcement",
            publishDate = "2024-03-21",
            publisher = "Peter Chan",
            publisherId = "23000"
        ),
        Announcement(
            id = 0,
            title = "Test Announcement1",
            content = "Test Announcement1",
            publishDate = "2024-03-21",
            publisher = "Peter Chan",
            publisherId = "23000"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            items(list) { announce ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = announce.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                HorizontalDivider()
            }
        }
    }
}