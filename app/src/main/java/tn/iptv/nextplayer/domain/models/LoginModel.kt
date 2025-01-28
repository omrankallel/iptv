package tn.iptv.nextplayer.domain.models

data class LoginModel(
    val action: String = "Login",
    val code: String,
    val mac: String,
    val model: String,
    val sn: String,
)