package tn.iptv.nextplayer.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import tn.iptv.nextplayer.core.ui.designsystem.animatedComposable
import tn.iptv.nextplayer.settings.Setting
import tn.iptv.nextplayer.settings.SettingsScreen

const val settingsNavigationRoute = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions? = navOptions { launchSingleTop = true }) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(onNavigateUp: () -> Unit, onItemClick: (Setting) -> Unit) {
    animatedComposable(route = settingsNavigationRoute) {
        SettingsScreen(onNavigateUp = onNavigateUp, onItemClick = onItemClick)
    }
}
