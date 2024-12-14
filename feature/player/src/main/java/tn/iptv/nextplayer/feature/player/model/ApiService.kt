// ApiService.kt
import retrofit2.http.GET
import tn.iptv.nextplayer.feature.player.model.ApiResponse

interface ApiService {


    @GET("Globaltvpro2025.json")
    suspend fun getServices(): ApiResponse


}
