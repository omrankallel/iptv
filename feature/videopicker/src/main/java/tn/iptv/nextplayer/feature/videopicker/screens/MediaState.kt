package tn.iptv.nextplayer.feature.videopicker.screens

import tn.iptv.nextplayer.core.model.Folder

sealed interface MediaState {
    data object Loading : MediaState
    data class Success(val data: Folder?) : MediaState
}
