package tn.iptv.nextplayer.feature.player.utils

object AppHelper {
    fun cleanChannelName(rawName: String): String {
        val regex = Regex(".*\\| [A-Z]{2}:\\s*|^[A-Z]{2}:\\s*")
        val regex2 = Regex(".*\\|\\s*")
        val value = rawName.replace(regex, "").trim()


        return value.replace(regex2, "").trim()
    }


}
