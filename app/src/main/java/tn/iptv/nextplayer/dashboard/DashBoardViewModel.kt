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

    private fun observeSeries() {
        channelManager.selectedPackageOfSeries.observeForever {
            bindingModel.selectedPackageOfSeries.value = it


        }

        channelManager.listGroupedSeriesByCategory.observeForever {
            bindingModel.listSeriesByCategory.value = it
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