package tn.iptv.nextplayer.domain

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import tn.iptv.nextplayer.domain.models.ChannelResponse



interface ChannelsApi {

    @GET("Gltvpromax.json")  //Globaltvpro2025.json
    suspend fun getChannel(): Response<ChannelResponse>


    @GET("authentification.php")
    fun loginChannelSHahidGtv(
        @Query("code") code: String,
        @Query("mac") mac: String,
        @Query("sn") sn: String,
        @Query("model") model: String,
    ): Call<ResponseBody>

    @GET("restore.php")
    fun restoreAccount(
        @Query("mac") mac: String,
        @Query("sn") sn: String,
        @Query("hash") hash: String,
    ): Call<ResponseBody>


    @GET("getPackages.php")
    fun getPackages(
        @Query("code") code: String,
    ): Call<ResponseBody>


    @GET("getCategoriesSeries.php")
    fun getCategoriesSeries(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getSeriesByCategory.php")
    fun getSeriesByCategory(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getMoviesByCategory.php")
    fun getMoviesByCategory(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getLiveChannelsByParentCategory.php")
    fun getLiveChannelByCategory(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getSaisonsBySerie.php")
    fun getSaisonsBySerie(
        @Query("id") id: String,
        @Query("code") code: String,
    ): Call<ResponseBody>


    //)http://onplus.me:6739/api-Gltv2/serie/getEpisodesBySerieAndSaison.php?id=7681&saison=2&code=045442217616
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getEpisodesBySerieAndSaison.php")
    fun getEpisodesBySerieAndSaison(
        @Query("id") id: String,
        @Query("code") code: String,
        @Query("saison") saison: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getCategoriesMovies.php")
    fun getCategoriesMovies(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("getCategoriesChannelsByParentID.php")
    fun getCategoriesLiveTV(
        @Query("id") id: Int,
        @Query("code") code: String,
    ): Call<ResponseBody>


    @GET("getCategoriesChannelsByParentID.php")
    fun getCategoriesChannelsByParentID(
        @Query("code") code: String,
        @Query("id") id: String,
    ): Call<ResponseBody>

}
