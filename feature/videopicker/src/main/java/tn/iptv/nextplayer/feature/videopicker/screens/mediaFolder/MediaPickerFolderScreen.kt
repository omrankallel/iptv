package tn.iptv.nextplayer.feature.videopicker.screens.mediaFolder

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tn.iptv.nextplayer.core.common.extensions.prettyName
import tn.iptv.nextplayer.core.model.ApplicationPreferences
import tn.iptv.nextplayer.core.model.Folder
import tn.iptv.nextplayer.core.ui.R
import tn.iptv.nextplayer.core.ui.components.NextTopAppBar
import tn.iptv.nextplayer.core.ui.designsystem.NextIcons
import tn.iptv.nextplayer.feature.videopicker.composables.MediaView
import tn.iptv.nextplayer.feature.videopicker.screens.MediaState
import java.io.File

@Composable
fun MediaPickerFolderRoute(
    viewModel: MediaPickerFolderViewModel = hiltViewModel(),
    onVideoClick: (uri: Uri) -> Unit,
    onFolderClick: (folderPath: String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    // The app experiences jank when videosState updates before the initial render finishes.
    // By adding Lifecycle.State.RESUMED, we ensure that we wait until the first render completes.
    val mediaState by viewModel.mediaState.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MediaPickerFolderScreen(
        folderPath = viewModel.folderPath,
        mediaState = mediaState,
        preferences = preferences,
        isRefreshing = uiState.refreshing,
        onPlayVideo = onVideoClick,
        onNavigateUp = onNavigateUp,
        onFolderClick = onFolderClick,
        onDeleteVideoClick = { viewModel.deleteVideos(listOf(it)) },
        onAddToSync = viewModel::addToMediaInfoSynchronizer,
        onRenameVideoClick = viewModel::renameVideo,
        onRefreshClicked = viewModel::onRefreshClicked,
        onDeleteFolderClick = { viewModel.deleteFolders(listOf(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MediaPickerFolderScreen(
    folderPath: String,
    mediaState: MediaState,
    preferences: ApplicationPreferences,
    isRefreshing: Boolean = false,
    onNavigateUp: () -> Unit,
    onPlayVideo: (Uri) -> Unit,
    onFolderClick: (String) -> Unit = {},
    onDeleteVideoClick: (String) -> Unit,
    onRenameVideoClick: (Uri, String) -> Unit = { _, _ -> },
    onAddToSync: (Uri) -> Unit,
    onRefreshClicked: () -> Unit = {},
    onDeleteFolderClick: (Folder) -> Unit = {},
) {
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefreshClicked()
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        topBar = {
            NextTopAppBar(
                title = File(folderPath).prettyName,
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = NextIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_up),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (!preferences.showFloatingPlayButton) return@Scaffold
            FloatingActionButton(
                onClick = {
                    val state = mediaState as? MediaState.Success
                    val videoToPlay = state?.data?.recentlyPlayedVideo ?: state?.data?.firstVideo
                    if (videoToPlay != null) {
                        onPlayVideo(Uri.parse(videoToPlay.uriString))
                    }
                },
            ) {
                Icon(
                    imageVector = NextIcons.Play,
                    contentDescription = null,
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
            contentAlignment = Alignment.Center,
        ) {
            MediaView(
                isLoading = mediaState is MediaState.Loading,
                rootFolder = (mediaState as? MediaState.Success)?.data,
                preferences = preferences,
                onFolderClick = onFolderClick,
                onDeleteFolderClick = onDeleteFolderClick,
                onVideoClick = onPlayVideo,
                onDeleteVideoClick = onDeleteVideoClick,
                onVideoLoaded = onAddToSync,
                onRenameVideoClick = onRenameVideoClick,
            )
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
