package tn.iptv.nextplayer.dashboard

import PreferencesHelper
import android.annotation.SuppressLint
import android.util.Log
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.MVVM.BaseViewModel
import tn.iptv.nextplayer.domain.channelManager.ChannelManager


class DashBoardViewModel : BaseViewModel() {

    lateinit var bindingModel: DashBoardBindingModel
    private val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)


    @SuppressLint("LogNotTimber")
    override fun initBindingData() {
        Log.d("dashboard_vm", " initBindingData")
        bindingModel = DashBoardBindingModel()
        channelManager.activationCode.value = PreferencesHelper.getActivationCode(app)
        channelManager.fetchPackages()
        observeHome()

        observeSeries()

        observeMovies()

        observeLiveTV()

        observeFavorite()

        channelManager.listOfFilterYear.value!!.clear()
        channelManager.listOfFilterGenre.value!!.clear()

        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        channelManager.listOfFilterYear.value = (1960..currentYear).map { it.toString() }.toMutableList()
        channelManager.listOfFilterYear.value!!.reverse()
        channelManager.listOfFilterYear.value!!.add(0, "Tous")
        channelManager.listOfFilterGenre.value!!.addAll(
            listOf(
                "دراما", "كوميديا", "مغامرة", "فانتازيا", "حركة", "جريمة", "إثارة", "موسيقى", "رومنسية",
                "عائلي", "تاريخ", "رعب", "خيال علمي", "وثائقي", "تشويق", "حرب", "غموض", "سياسة",
                "سيرة ذاتية", "أكشن", "كوارث", "رومانسي", "تاريخي", "موسيقية", "خيال", "إثارة نفسية",
                "دراما كوميدية", "كوميديا سوداء", "تسجيلي", "دراما رومانسية", "دراما تاريخية",
                "دراما حرب", "دراما اجتماعية", "خيال علمي درامي", "دراما عائلية", "مغامرات كوميدية",
                "action", "adventure", "animation", "crime", "documentaire", "drama", "drame", "fantastique", "Comédie", "War & Politics",
                "familial", "histoire", "horreur", "mystère", "romance", "science-fiction", "thriller", "western", "Action & Adventure", "Comedy", "Divertissement", "Mystery", "Reality",
                "Documentary", "Family", "Soap", "Aksiyon & Macera", "Aile", "Suç", "Pembe Dizi", "Savaş & Politik", "Bilim Kurgu & Fantazi", "Gizem",
            ),
        )
        channelManager.listOfFilterGenre.value!!.add(0, "Tous")


    }

    private fun observeLiveTV() {

        channelManager.liveTVIsLoading.observeForever {
            bindingModel.isLoadingLiveTV.value = it
        }

        channelManager.listGroupedLiveTVByCategory.observeForever {
            bindingModel.listLiveByCategory.value = it
        }

        channelManager.selectedPackageOfLiveTV.observeForever {
            bindingModel.selectedPackageOfLiveTV.value = it

        }

    }

    private fun observeMovies() {
        channelManager.moviesIsLoading.observeForever {
            bindingModel.isLoadingMovies.value = it
        }

        channelManager.listGroupedMovieByCategory.observeForever {
            bindingModel.listMoviesByCategory.value = it
            bindingModel.listMoviesByCategoryFiltered.value = it
        }

        channelManager.selectedPackageOfMovies.observeForever {
            bindingModel.selectedPackageOfMovies.value = it

        }
    }

    private fun observeHome() {


        channelManager.homeIsLoading.observeForever {
            bindingModel.isLoadingHome.value = it
        }
    }

    private fun observeFavorite() {


        channelManager.favoriteIsLoading.observeForever {
            bindingModel.isLoadingFavorite.value = it
        }
        channelManager.listFavorites.observeForever {
            bindingModel.listFavoriteFiltered.value = it
        }
    }

    private fun observeSeries() {
        channelManager.selectedPackageOfSeries.observeForever {
            bindingModel.selectedPackageOfSeries.value = it


        }

        channelManager.listGroupedSeriesByCategory.observeForever {
            bindingModel.listSeriesByCategory.value = it
            bindingModel.listSeriesByCategoryFiltered.value = it
        }

        channelManager.seriesIsLoading.observeForever {
            bindingModel.isLoadingSeries.value = it
        }

        channelManager.listOfSaisonBySerie.observeForever {
            bindingModel.listSaisonOfSerie.value = it
        }

        channelManager.listGroupedEpisodeBySaison.observeForever {
            bindingModel.listEpisodeGroupedBySeason.value = it
        }
    }


}