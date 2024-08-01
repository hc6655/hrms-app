package com.hkbufyp.hrms.ui.screen.announcement.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnnounceDetailScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    announceDetailViewModel: AnnounceDetailViewModel
) {

    val uiState by announceDetailViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Announcement")

        announceDetailViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
                onRefresh = {
                    announceDetailViewModel.onEvent(AnnounceDetailEvent.Refresh)
                }
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = uiState.announcement?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp, top = 5.dp)
                    ) {
                        Text(
                            text = "Publisher: ",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Text(
                            text = uiState.announcement?.publisher ?: "",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = uiState.announcement?.publishDate ?: "",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 15.dp)
                    ) {
                        Text(
                            text = uiState.announcement?.content ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {

}