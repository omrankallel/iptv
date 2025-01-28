package tn.iptv.nextplayer.domain.models.languages

import fetchString
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.login.app


enum class LanguageItem(
    val label: String,
    val selected: Boolean = false,
) {

    FRENCH(
        label = fetchString(app, R.string.french),
    ),

    ANGLAIS(
        label = fetchString(app, R.string.anglais),
        selected = true,
    ),

    DEUTCH(
        label = fetchString(app, R.string.deutch),
    ),

    ITALIAN(
        label = fetchString(app, R.string.italian),
    ),

    ESPAGNOL(
        label = fetchString(app, R.string.espagnol),
    ),


}