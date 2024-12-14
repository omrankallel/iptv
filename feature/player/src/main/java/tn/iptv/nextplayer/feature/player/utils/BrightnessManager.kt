package tn.iptv.nextplayer.feature.player.utils

import android.view.WindowManager
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.player.extensions.currentBrightness
import tn.iptv.nextplayer.feature.player.extensions.swipeToShowStatusBars

class BrightnessManager(private val activity: PlayerActivity) {

    var currentBrightness = activity.currentBrightness
    val maxBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL

    val brightnessPercentage get() = (currentBrightness / maxBrightness).times(100).toInt()

    fun setBrightness(brightness: Float) {
        currentBrightness = brightness.coerceIn(0f, maxBrightness)
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = currentBrightness
        activity.window.attributes = layoutParams

        // fixes a bug which makes the action bar reappear after changing the brightness
        activity.swipeToShowStatusBars()
    }
}
