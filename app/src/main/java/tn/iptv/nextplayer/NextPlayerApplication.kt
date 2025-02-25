package tn.iptv.nextplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import tn.iptv.nextplayer.core.common.di.ApplicationScope
import tn.iptv.nextplayer.core.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltAndroidApp
class NextPlayerApplication : Application() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()

        // Preloading updated preferences to ensure that the player uses the latest preferences set by the user.
        // This resolves the issue where player use default preferences upon launching the app from a cold start.
        // See [the corresponding issue for more info](https://github.com/anilbeesetti/nextplayer/issues/392)
        applicationScope.launch {
            preferencesRepository.applicationPreferences.first()
            preferencesRepository.playerPreferences.first()
        }

        startKoin {
            androidLogger()
            androidContext(this@NextPlayerApplication)
            modules(appModules, viewModelModules)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
