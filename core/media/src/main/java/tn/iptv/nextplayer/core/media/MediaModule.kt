package tn.iptv.nextplayer.core.media

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tn.iptv.nextplayer.core.media.services.LocalMediaService
import tn.iptv.nextplayer.core.media.services.MediaService
import tn.iptv.nextplayer.core.media.sync.LocalMediaInfoSynchronizer
import tn.iptv.nextplayer.core.media.sync.LocalMediaSynchronizer
import tn.iptv.nextplayer.core.media.sync.MediaInfoSynchronizer
import tn.iptv.nextplayer.core.media.sync.MediaSynchronizer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MediaModule {

    @Binds
    @Singleton
    fun bindsMediaSynchronizer(
        mediaSynchronizer: LocalMediaSynchronizer,
    ): MediaSynchronizer

    @Binds
    @Singleton
    fun bindsMediaInfoSynchronizer(
        mediaInfoSynchronizer: LocalMediaInfoSynchronizer,
    ): MediaInfoSynchronizer

    @Binds
    @Singleton
    fun bindMediaService(
        mediaService: LocalMediaService,
    ): MediaService
}
