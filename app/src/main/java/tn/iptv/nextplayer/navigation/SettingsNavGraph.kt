package tn.iptv.nextplayer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import tn.iptv.nextplayer.settings.Setting
import tn.iptv.nextplayer.settings.navigation.aboutPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.appearancePreferencesScreen
import tn.iptv.nextplayer.settings.navigation.audioPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.decoderPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.folderPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.mediaLibraryPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.navigateToAboutPreferences
import tn.iptv.nextplayer.settings.navigation.navigateToAppearancePreferences
import tn.iptv.nextplayer.settings.navigation.navigateToAudioPreferences
import tn.iptv.nextplayer.settings.navigation.navigateToDecoderPreferences
import tn.iptv.nextplayer.settings.navigation.navigateToFolderPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.navigateToMediaLibraryPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.navigateToPlayerPreferences
import tn.iptv.nextplayer.settings.navigation.navigateToSubtitlePreferences
import tn.iptv.nextplayer.settings.navigation.playerPreferencesScreen
import tn.iptv.nextplayer.settings.navigation.settingsNavigationRoute
import tn.iptv.nextplayer.settings.navigation.settingsScreen
import tn.iptv.nextplayer.settings.navigation.subtitlePreferencesScreen

const val SETTINGS_ROUTE = "settings_nav_route"

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = settingsNavigationRoute,
        route = SETTINGS_ROUTE,
    ) {
        settingsScreen(
            onNavigateUp = navController::navigateUp,
            onItemClick = { setting ->
                when (setting) {
                    Setting.APPEARANCE -> navController.navigateToAppearancePreferences()
                    Setting.MEDIA_LIBRARY -> navController.navigateToMediaLibraryPreferencesScreen()
                    Setting.PLAYER -> navController.navigateToPlayerPreferences()
                    Setting.DECODER -> navController.navigateToDecoderPreferences()
                    Setting.AUDIO -> navController.navigateToAudioPreferences()
                    Setting.SUBTITLE -> navController.navigateToSubtitlePreferences()
                    Setting.ABOUT -> navController.navigateToAboutPreferences()
                }
            },
        )
        appearancePreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        mediaLibraryPreferencesScreen(
            onNavigateUp = navController::navigateUp,
            onFolderSettingClick = navController::navigateToFolderPreferencesScreen,
        )
        folderPreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        playerPreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        decoderPreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        audioPreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        subtitlePreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
        aboutPreferencesScreen(
            onNavigateUp = navController::navigateUp,
        )
    }
}
