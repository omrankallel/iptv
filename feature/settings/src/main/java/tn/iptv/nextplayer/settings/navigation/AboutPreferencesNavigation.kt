package tn.iptv.nextplayer.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import tn.iptv.nextplayer.core.ui.designsystem.animatedComposable
import tn.iptv.nextplayer.settings.screens.about.AboutPreferencesScreen

const val aboutPreferencesNavigationRoute = "about_preferences_route"

fun NavController.navigateToAboutPreferences(navOptions: NavOptions? = navOptions { launchSingleTop = true }) {
    this.navigate(aboutPreferencesNavigationRoute, navOptions)
}

fun NavGraphBuilder.aboutPreferencesScreen(onNavigateUp: () -> Unit) {
    animatedComposable(route = aboutPreferencesNavigationRoute) {
        AboutPreferencesScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}
