package tn.iptv.nextplayer.feature.player

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import tn.iptv.nextplayer.core.data.models.VideoState
import tn.iptv.nextplayer.core.data.repository.MediaRepository
import tn.iptv.nextplayer.core.data.repository.PreferencesRepository
import tn.iptv.nextplayer.core.domain.GetSortedPlaylistUseCase
import tn.iptv.nextplayer.core.model.Resume
import tn.iptv.nextplayer.core.model.VideoZoom
import tn.iptv.nextplayer.feature.player.extensions.isSchemaContent
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tn.iptv.nextplayer.feature.player.model.Product
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val END_POSITION_OFFSET = 5L

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val preferencesRepository: PreferencesRepository,
    private val getSortedPlaylistUseCase: GetSortedPlaylistUseCase,
) : ViewModel() {

    var currentPlaybackPosition: Long? = null
    var currentPlaybackSpeed: Float = 1f
    var currentAudioTrackIndex: Int? = null
    var currentSubtitleTrackIndex: Int? = null
    var currentVideoScale: Float = 1f
    var isPlaybackSpeedChanged: Boolean = false
    val externalSubtitles = mutableSetOf<Uri>()
    var skipSilenceEnabled: Boolean = false


    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val apiService = RetrofitInstance.api

    private var currentVideoState: VideoState? = null

    val playerPrefs = preferencesRepository.playerPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking { preferencesRepository.playerPreferences.first() },
    )

    val appPrefs = preferencesRepository.applicationPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking { preferencesRepository.applicationPreferences.first() },
    )

    suspend fun initMediaState(uri: String?) {
        if (currentPlaybackPosition != null) return
        currentVideoState = uri?.let { mediaRepository.getVideoState(it) }
        val prefs = playerPrefs.value

        currentPlaybackPosition = currentVideoState?.position.takeIf { prefs.resume == Resume.YES } ?: currentPlaybackPosition
        currentAudioTrackIndex = currentVideoState?.audioTrackIndex.takeIf { prefs.rememberSelections } ?: currentAudioTrackIndex
        currentSubtitleTrackIndex = currentVideoState?.subtitleTrackIndex.takeIf { prefs.rememberSelections } ?: currentSubtitleTrackIndex
        currentPlaybackSpeed = currentVideoState?.playbackSpeed.takeIf { prefs.rememberSelections } ?: prefs.defaultPlaybackSpeed
        currentVideoScale = currentVideoState?.videoScale.takeIf { prefs.rememberSelections } ?: 1f
        externalSubtitles += currentVideoState?.externalSubs ?: emptyList()
    }

    suspend fun getPlaylistFromUri(uri: Uri): List<Uri> {
        return getSortedPlaylistUseCase.invoke(uri)
    }


    fun fetchProducts() {
    }

    fun saveState(
        uri: Uri,
        position: Long,
        duration: Long,
        audioTrackIndex: Int,
        subtitleTrackIndex: Int,
        playbackSpeed: Float,
        skipSilence: Boolean,
        videoScale: Float,
    ) {
        currentPlaybackPosition = position
        currentAudioTrackIndex = audioTrackIndex
        currentSubtitleTrackIndex = subtitleTrackIndex
        currentPlaybackSpeed = playbackSpeed
        currentVideoScale = videoScale
        skipSilenceEnabled = skipSilence

        if (!uri.isSchemaContent) return

        val newPosition = position.takeIf {
            position < duration - END_POSITION_OFFSET
        } ?: C.TIME_UNSET

        viewModelScope.launch {
            mediaRepository.saveVideoState(
                uri = uri.toString(),
                position = newPosition,
                audioTrackIndex = audioTrackIndex,
                subtitleTrackIndex = subtitleTrackIndex,
                playbackSpeed = playbackSpeed.takeIf { isPlaybackSpeedChanged } ?: currentVideoState?.playbackSpeed,
                externalSubs = externalSubtitles.toList(),
                videoScale = videoScale,
            )
        }
    }

    fun setPlayerBrightness(value: Float) {
        viewModelScope.launch {
            preferencesRepository.updatePlayerPreferences { it.copy(playerBrightness = value) }
        }
    }

    fun setVideoZoom(videoZoom: VideoZoom) {
        viewModelScope.launch {
            preferencesRepository.updatePlayerPreferences { it.copy(playerVideoZoom = videoZoom) }
        }
    }

    fun resetAllToDefaults() {
        currentPlaybackPosition = null
        currentPlaybackSpeed = 1f
        currentAudioTrackIndex = null
        currentSubtitleTrackIndex = null
        isPlaybackSpeedChanged = false
        currentVideoScale = 1f
        skipSilenceEnabled = false
        externalSubtitles.clear()
    }
}
