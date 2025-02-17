package tn.iptv.nextplayer.domain.channelManager

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.core.data.models.Favorite
import tn.iptv.nextplayer.domain.models.Channel
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.LoginModel
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem
import tn.iptv.nextplayer.domain.models.episode.GroupedEpisode
import tn.iptv.nextplayer.domain.models.packages.CategoryMedia
import tn.iptv.nextplayer.domain.models.packages.PackageMedia
import tn.iptv.nextplayer.domain.models.packages.ResponsePackageItem
import tn.iptv.nextplayer.domain.models.saisons.SaisonItem
import tn.iptv.nextplayer.domain.models.series.MediaItem


interface ChannelManager {
    companion object {
        const val cryptKey = "Kjr11#aq_X1M@38N+SZ2/1R44"
    }



    /**
     * [ChannelImp.listOfFilterYear]
     * */
    var listOfFilterYear: MutableLiveData<MutableList<String>>


    /**
     * [ChannelImp.listOfFilterGenre]
     * */
    var listOfFilterGenre: MutableLiveData<MutableList<String>>



    /**
     * [ChannelImp.channelSelected]
     * */
    var channelSelected: MutableLiveData<Channel>


    /**
     * [ChannelImp.activationCode]
     * */
    var activationCode: MutableLiveData<String>


    /**
     * [ChannelImp.searchValue]
     * */
    var searchValue: MutableLiveData<String>


    /**
     * [ChannelImp.listOfChannels]
     * */
    var listOfChannels: MutableLiveData<MutableList<Channel>>


    /**
     * [ChannelImp.listOfPackages]
     * */
    var listOfPackages: MutableLiveData<MutableList<ResponsePackageItem>>



    /////////////////////////////   Live TV   ///////////////////////////////
    /**
     * [ChannelImp.listOfPackagesOfLiveTV]
     * */
    var listOfPackagesOfLiveTV: MutableLiveData<MutableList<PackageMedia>>

    /**
     * [ChannelImp.selectedPackageOfLiveTV]
     * */
    var selectedPackageOfLiveTV: MutableLiveData<PackageMedia>


    /////////////////////////////   Movies     ///////////////////////////////
    /**
     * [ChannelImp.listOfPackagesOfMovies]
     * */
    var listOfPackagesOfMovies: MutableLiveData<MutableList<PackageMedia>>

    /**
     * [ChannelImp.showAllStateMovies]
     * */
    var showAllStateMovies: MutableStateFlow<GroupedMedia?>

    /**
     * [ChannelImp.showAllStateMoviesFiltered]
     * */
    var showAllStateMoviesFiltered: MutableStateFlow<GroupedMedia?>

    /**
     * [ChannelImp.selectedPackageOfMovies]
     * */
    var selectedPackageOfMovies: MutableLiveData<PackageMedia>

    /**
     * [ChannelImp.movieSelected]
     * */
    var movieSelected: MutableLiveData<MediaItem>


    /////////////////////////////    Series   ///////////////////////////////
    /**
     * [ChannelImp.listOfPackagesOfSeries]
     * */
    var listOfPackagesOfSeries: MutableLiveData<MutableList<PackageMedia>>

    /**
     * [ChannelImp.selectedPackageOfSeries]
     * */
    var selectedPackageOfSeries: MutableLiveData<PackageMedia>


    /**
     * [ChannelImp.showAllStateSeries]
     * */
    var showAllStateSeries: MutableStateFlow<GroupedMedia?>

    /**
     * [ChannelImp.showAllStateSeriesFiltered]
     * */
    var showAllStateSeriesFiltered: MutableStateFlow<GroupedMedia?>


    /**
     * [ChannelImp.selectedEpisodeToWatch]
     * */
    var selectedEpisodeToWatch: MutableLiveData<EpisodeItem>

    /**
     * [ChannelImp.seriesIsLoading]
     * */
    var seriesIsLoading: MutableLiveData<Boolean>

    /**
     * [ChannelImp.homeIsLoading]
     * */
    var homeIsLoading: MutableLiveData<Boolean>


    /**
     * [ChannelImp.channelIsLoading]
     * */
    var channelIsLoading: MutableLiveData<Boolean>

    /**
     * [ChannelImp.favoriteIsLoading]
     * */
    var favoriteIsLoading: MutableLiveData<Boolean>



    /**
     * [ChannelImp.moviesIsLoading]
     * */
    var moviesIsLoading: MutableLiveData<Boolean>


    /**
     * [ChannelImp.liveTVIsLoading]
     * */
    var liveTVIsLoading: MutableLiveData<Boolean>


    /**
     * [ChannelImp.listGroupedSeriesByCategory]
     * */
    var listGroupedSeriesByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.allListGroupedSeriesByCategory]
     * */
    var allListGroupedSeriesByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.listGroupedMovieByCategory]
     * */
    var listGroupedMovieByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.allListGroupedMovieByCategory]
     * */
    var allListGroupedMovieByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.listFavorites]
     * */
    var listFavorites: MutableLiveData<MutableList<Favorite>>

    /**
     * [ChannelImp.allListFavorites]
     * */
    var allListFavorites: MutableLiveData<MutableList<Favorite>>


    /**
     * [ChannelImp.showAllStateFavorites]
     * */
    var showAllStateFavorites: MutableStateFlow<List<Favorite>>

    /**
     * [ChannelImp.showAllStateFavoritesFiltered]
     * */
    var showAllStateFavoritesFiltered: MutableStateFlow<List<Favorite>>




    /**
     * [ChannelImp.listGroupedLiveTVByCategory]
     * */
    var listGroupedLiveTVByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.allListGroupedLiveTVByCategory]
     * */
    var allListGroupedLiveTVByCategory: MutableLiveData<MutableList<GroupedMedia>>


    /**
     * [ChannelImp.liveSelected]
     * */
    var liveSelected: MutableLiveData<PackageMedia>


    /**
     *  * [ChannelImp.homeSelected]
     * */
    var homeSelected: MutableLiveData<CategoryMedia>


    /**
     * [ChannelImp.serieSelected]
     * */
    var serieSelected: MutableLiveData<MediaItem>


    /**
     * [ChannelImp.listOfSaisonBySerie]
     * */
    var listOfSaisonBySerie: MutableLiveData<MutableList<SaisonItem>>


    /**
     * [ChannelImp.listGroupedEpisodeBySaison]
     * */
    var listGroupedEpisodeBySaison: MutableLiveData<MutableList<GroupedEpisode>>


    /**
     * [ChannelImp.fetchListChannels]
     * */
    fun fetchListChannels()


    /**
     * [ChannelImp.newLoginToChannel]
     * */
    fun newLoginToChannel(loginModel: LoginModel, fullUrl: String, onSuccess: () -> Unit, onFailure: () -> Unit)

    /**
     * [ChannelImp.restoreAccount]
     * */
    fun restoreAccount(mac: String, sn: String, fullUrl: String, onSuccess: (String) -> Unit, onFailure: () -> Unit)


    /**
     * [ChannelImp.fetchPackages]
     * */
    fun fetchPackages()

    /**
     * [ChannelImp.fetchFavorites]
     * */
    fun fetchFavorites(favoriteViewModel: FavoriteViewModel,searchValue: String)


    /**
     * [ChannelImp.fetchCategorySeriesAndSeries]
     * */
    fun fetchCategorySeriesAndSeries(searchValue: String)


    /**
     * [ChannelImp.searchCategorySeriesAndSeries]
     * */
    fun searchCategorySeriesAndSeries(searchValue: String)


    /**
     * [ChannelImp.fetchCategoryMoviesAndMovies]
     * */
    fun fetchCategoryMoviesAndMovies(searchValue: String)

    /**
     * [ChannelImp.searchCategoryMoviesAndMovies]
     * */
    fun searchCategoryMoviesAndMovies(searchValue: String)


    /**
     * [ChannelImp.searchCategoryFavoritesAndFavorites]
     * */
    fun searchCategoryFavoritesAndFavorites(searchValue: String)


    /**
     * [ChannelImp.fetchCategoryLiveTVAndLiveTV]
     * */
    fun fetchCategoryLiveTVAndLiveTV(searchValue: String)

    /**
     * [ChannelImp.fetchCategoryLiveTVAndLiveTV]
     * */
    fun searchForCategoryLiveTVAndLiveTV(searchValue: String)


    /**
     * [ChannelImp.fetchSaisonBySerie]
     * */
    fun fetchSaisonBySerie()


    /**
     * [ChannelImp.getCategoryLiveByGroupID]
     * */
    fun getCategoryLiveByGroupID()


}