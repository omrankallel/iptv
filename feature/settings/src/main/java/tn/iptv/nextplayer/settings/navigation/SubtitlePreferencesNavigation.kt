package tn.iptv.nextplayer.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import tn.iptv.nextplayer.core.ui.designsystem.animatedComposable
import tn.iptv.nextplayer.settings.screens.subtitle.SubtitlePreferencesScreen

const val subtitlePreferencesNavigationRoute = "subtitle_preferences_route"

fun NavController.navigateToSubtitlePreferences(navOptions: NavOptions? = navOptions { launchSingleTop = true }) {
    this.navigate(subtitlePreferencesNavigationRoute, navOptions)
}

fun NavGraphBuilder.subtitlePreferencesScreen(onNavigateUp: () -> Unit) {
    animatedComposable(route = subtitlePreferencesNavigationRoute) {
        SubtitlePreferencesScreen(onNavigateUp = onNavigateUp)
    }
}
