package tn.iptv.nextplayer.core.data.repository.fake

import android.net.Uri
import tn.iptv.nextplayer.core.data.models.VideoState
import tn.iptv.nextplayer.core.data.repository.MediaRepository
import tn.iptv.nextplayer.core.model.Folder
import tn.iptv.nextplayer.core.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMediaRepository : MediaRepository {

    val videos = mutableListOf<Video>()
    val directories = mutableListOf<Folder>()

    override fun getVideosFlow(): Flow<List<Video>> {
        return flowOf(videos)
    }

    override fun getVideosFlowFromFolderPath(folderPath: String): Flow<List<Video>> {
        return flowOf(videos)
    }

    override fun getFoldersFlow(): Flow<List<Folder>> {
        return flowOf(directories)
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
    }

    override suspend fun getVideoState(uri: String): VideoState? {
        return null
    }
}
