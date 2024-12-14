package tn.iptv.nextplayer.core.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tn.iptv.nextplayer.core.data.repository.LocalMediaRepository
import tn.iptv.nextplayer.core.data.repository.LocalPreferencesRepository
import tn.iptv.nextplayer.core.data.repository.MediaRepository
import tn.iptv.nextplayer.core.data.repository.PreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsMediaRepository(
        videoRepository: LocalMediaRepository,
    ): MediaRepository

    @Binds
    fun bindsPreferencesRepository(
        preferencesRepository: LocalPreferencesRepository,
    ): PreferencesRepository
}
