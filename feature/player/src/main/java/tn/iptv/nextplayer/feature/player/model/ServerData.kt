package tn.iptv.nextplayer.feature.player.model


// ServiceData.kt
data class ServerData(
    val id: String,
    val name: String,
    val url: String,
    val icon: String
)

// ApiResponse.kt
data class ApiResponse(
    val response: Boolean,
    val data: List<ServerData>
)
