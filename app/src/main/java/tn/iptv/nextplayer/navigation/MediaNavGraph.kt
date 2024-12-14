package tn.iptv.nextplayer.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.videopicker.navigation.mediaPickerFolderScreen
import tn.iptv.nextplayer.feature.videopicker.navigation.mediaPickerNavigationRoute
import tn.iptv.nextplayer.feature.videopicker.navigation.mediaPickerScreen
import tn.iptv.nextplayer.feature.videopicker.navigation.navigateToMediaPickerFolderScreen
import tn.iptv.nextplayer.settings.navigation.navigateToSettings

const val MEDIA_ROUTE = "media_nav_route"

fun NavGraphBuilder.mediaNavGraph(
    context: Context,
    navController: NavHostController,
) {
    navigation(
        startDestination = mediaPickerNavigationRoute,
        route = MEDIA_ROUTE,
    ) {
        mediaPickerScreen(
            onPlayVideo = context::startPlayerActivity,
            onFolderClick = navController::navigateToMediaPickerFolderScreen,
            onSettingsClick = navController::navigateToSettings,
        )
        mediaPickerFolderScreen(
            onNavigateUp = navController::navigateUp,
            onVideoClick = context::startPlayerActivity,
            onFolderClick = navController::navigateToMediaPickerFolderScreen,
        )
    }
}

fun Context.startPlayerActivity(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri, this, PlayerActivity::class.java)
    startActivity(intent)
}
