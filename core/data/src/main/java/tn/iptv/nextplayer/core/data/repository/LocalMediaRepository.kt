package tn.iptv.nextplayer.core.data.repository

import android.net.Uri
import tn.iptv.nextplayer.core.common.di.ApplicationScope
import tn.iptv.nextplayer.core.data.mappers.toFolder
import tn.iptv.nextplayer.core.data.mappers.toVideo
import tn.iptv.nextplayer.core.data.mappers.toVideoState
import tn.iptv.nextplayer.core.data.models.VideoState
import tn.iptv.nextplayer.core.database.converter.UriListConverter
import tn.iptv.nextplayer.core.database.dao.DirectoryDao
import tn.iptv.nextplayer.core.database.dao.MediumDao
import tn.iptv.nextplayer.core.database.relations.DirectoryWithMedia
import tn.iptv.nextplayer.core.database.relations.MediumWithInfo
import tn.iptv.nextplayer.core.model.Folder
import tn.iptv.nextplayer.core.model.Video
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class LocalMediaRepository @Inject constructor(
    private val mediumDao: MediumDao,
    private val directoryDao: DirectoryDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : MediaRepository {

    override fun getVideosFlow(): Flow<List<Video>> {
        return mediumDao.getAllWithInfo().map { it.map(MediumWithInfo::toVideo) }
    }

    override fun getVideosFlowFromFolderPath(folderPath: String): Flow<List<Video>> {
        return mediumDao.getAllWithInfoFromDirectory(folderPath).map { it.map(MediumWithInfo::toVideo) }
    }

    override fun getFoldersFlow(): Flow<List<Folder>> {
        return directoryDao.getAllWithMedia().map { it.map(DirectoryWithMedia::toFolder) }
    }

    override suspend fun getVideoState(uri: String): VideoState? {
        return mediumDao.get(uri)?.toVideoState()
    }

    override suspend fun saveVideoState(
        uri: String,
        position: Long,
        audioTrackIndex: Int?,
        subtitleTrackIndex: Int?,
        playbackSpeed: Float?,
        externalSubs: List<Uri>,
        videoScale: Float,
    ) {
        Timber.d(
            "save state for [$uri]: [$position, $audioTrackIndex, $subtitleTrackIndex, $playbackSpeed]",
        )

        applicationScope.launch {
            mediumDao.updateMediumState(
                uri = uri,
                position = position,
                audioTrackIndex = audioTrackIndex,
                subtitleTrackIndex = subtitleTrackIndex,
                playbackSpeed = playbackSpeed,
                externalSubs = UriListConverter.fromListToString(externalSubs),
                lastPlayedTime = System.currentTimeMillis(),
                videoScale = videoScale,
            )
        }
    }
}
