package tn.iptv.nextplayer.core.data.repository

import tn.iptv.nextplayer.core.datastore.datasource.AppPreferencesDataSource
import tn.iptv.nextplayer.core.datastore.datasource.PlayerPreferencesDataSource
import tn.iptv.nextplayer.core.model.ApplicationPreferences
import tn.iptv.nextplayer.core.model.PlayerPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LocalPreferencesRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
    private val playerPreferencesDataSource: PlayerPreferencesDataSource,
) : PreferencesRepository {
    override val applicationPreferences: Flow<ApplicationPreferences>
        get() = appPreferencesDataSource.preferences

    override val playerPreferences: Flow<PlayerPreferences>
        get() = playerPreferencesDataSource.preferences

    override suspend fun updateApplicationPreferences(
        transform: suspend (ApplicationPreferences) -> ApplicationPreferences,
    ) {
        appPreferencesDataSource.update(transform)
    }

    override suspend fun updatePlayerPreferences(
        transform: suspend (PlayerPreferences) -> PlayerPreferences,
    ) {
        playerPreferencesDataSource.update(transform)
    }
}
