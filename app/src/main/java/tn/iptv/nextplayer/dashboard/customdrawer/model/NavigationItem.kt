package tn.iptv.nextplayer.dashboard.customdrawer.model

import tn.iptv.nextplayer.R


enum class NavigationItem(
    val title: String,
    val icon: Int,
) {
    Home(
        icon = R.drawable.ic_home,
        title = "Home",
    ),
    TVChannels(
        icon = R.drawable.ic_tv_channles,
        title = "Live TV",
    ),

    Series(
        icon = R.drawable.ic_series,
        title = "Series",
    ),

    DetailSeries(
        icon = R.drawable.ic_series,
        title = "Series",
    ),


    Movies(
        icon = R.drawable.icl_movies,
        title = "Movies",
    ),


    DetailMovies(
        icon = R.drawable.icl_movies,
        title = "Movies",
    ),

    Favorite(
        icon = R.drawable.ic_favorite,
        title = "Favorite",
    ),

    /*ComingSoon(
        icon = R.drawable.ic_coming_soon,
        title = "Coming Soon"
    ),*/

    Settings(
        icon = R.drawable.ic_settings,
        title = "Settings",
    ),

    Logout(
        icon = R.drawable.ic_logout,
        title = "Logout",
    )
}