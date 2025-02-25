package tn.iptv.nextplayer.dashboard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem
import tn.iptv.nextplayer.domain.models.episode.GroupedEpisode
import tn.iptv.nextplayer.domain.models.packages.PackageMedia
import tn.iptv.nextplayer.domain.models.saisons.SaisonItem
import tn.iptv.nextplayer.domain.models.series.MediaItem


class DashBoardBindingModel {


    var index: MutableState<Int> = mutableIntStateOf(0)


    var showFilters: MutableState<Boolean> = mutableStateOf(false)
    var selectedFilteredYear: MutableState<String> = mutableStateOf("Tous")
    var selectedFilteredGenre: MutableState<String> = mutableStateOf("Tous")

    var selectedPage = Page.NOTHING
    var selectedNavigationItem: MutableState<NavigationItem> = mutableStateOf(NavigationItem.Home)


    var isLoadingHome: MutableState<Boolean> = mutableStateOf(false)
    var isLoadingFavorite: MutableState<Boolean> = mutableStateOf(false)


    /////////////:  series  ///////////////
    var selectedPackageOfSeries: MutableState<PackageMedia> = mutableStateOf(PackageMedia())
    var listSeriesByCategoryFiltered: MutableState<List<GroupedMedia>> = mutableStateOf(listOf())
    var listSeriesByCategory: MutableState<List<GroupedMedia>> = mutableStateOf(listOf())
    var isLoadingSeries: MutableState<Boolean> = mutableStateOf(false)
    var selectedSerie: MutableState<MediaItem> = mutableStateOf(MediaItem())


    /////////// EPISODE //////////////
    var selectedEpisodeToWatch: MutableState<EpisodeItem> = mutableStateOf(EpisodeItem())


    var listSaisonOfSerie: MutableState<List<SaisonItem>> = mutableStateOf(listOf())


    var listEpisodeGroupedBySeason: MutableState<List<GroupedEpisode>> = mutableStateOf(listOf())


    /////////////  Movies  ///////////////
    var selectedPackageOfMovies: MutableState<PackageMedia> = mutableStateOf(PackageMedia())
    var listMoviesByCategoryFiltered: MutableState<List<GroupedMedia>> = mutableStateOf(listOf())
    var listMoviesByCategory: MutableState<List<GroupedMedia>> = mutableStateOf(listOf())
    var isLoadingMovies: MutableState<Boolean> = mutableStateOf(false)
    var selectedMovie: MutableState<MediaItem> = mutableStateOf(MediaItem())


    //////////////////   Live TV  ////////////
    var selectedPackageOfLiveTV: MutableState<PackageMedia> = mutableStateOf(PackageMedia())
    var listLiveByCategory: MutableState<List<GroupedMedia>> = mutableStateOf(listOf())
    var isLoadingLiveTV: MutableState<Boolean> = mutableStateOf(false)
    var selectedLiveTV: MutableState<MediaItem> = mutableStateOf(MediaItem())


}