package tn.iptv.nextplayer.feature.player.extensions

import tn.iptv.nextplayer.core.model.FastSeek
import tn.iptv.nextplayer.core.model.PlayerPreferences

fun PlayerPreferences.shouldFastSeek(duration: Long): Boolean {
    return when (fastSeek) {
        FastSeek.ENABLE -> true
        FastSeek.DISABLE -> false
        FastSeek.AUTO -> duration >= minDurationForFastSeek
    }
}
