package tn.iptv.nextplayer.domain.models.packages

data class ResponsePackageItem(
    val icon: String,
    val name: String,
    val screen: String,


    val live: List<PackageMedia>,
    val movies: List<PackageMedia>,
    val series: List<PackageMedia>,
)