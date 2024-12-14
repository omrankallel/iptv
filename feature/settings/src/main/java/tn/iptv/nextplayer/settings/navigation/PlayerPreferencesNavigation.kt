package tn.iptv.nextplayer.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import tn.iptv.nextplayer.core.ui.designsystem.animatedComposable
import tn.iptv.nextplayer.settings.screens.player.PlayerPreferencesScreen

const val playerPreferencesNavigationRoute = "player_preferences_route"

fun NavController.navigateToPlayerPreferences(navOptions: NavOptions? = navOptions { launchSingleTop = true }) {
    this.navigate(playerPreferencesNavigationRoute, navOptions)
}

fun NavGraphBuilder.playerPreferencesScreen(onNavigateUp: () -> Unit) {
    animatedComposable(route = playerPreferencesNavigationRoute) {
        PlayerPreferencesScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}
