package tn.iptv.nextplayer.domain.channelManager

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.awaitResponse
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.core.data.models.Favorite
import tn.iptv.nextplayer.domain.channelManager.ChannelManager.Companion.cryptKey
import tn.iptv.nextplayer.domain.config.AES256Cipher
import tn.iptv.nextplayer.domain.config.RetrofitInstance
import tn.iptv.nextplayer.domain.models.Channel
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.LoginModel
import tn.iptv.nextplayer.domain.models.category.ResponseCategory
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem
import tn.iptv.nextplayer.domain.models.episode.GroupedEpisode
import tn.iptv.nextplayer.domain.models.episode.ResponseListEpisode
import tn.iptv.nextplayer.domain.models.packages.CategoryMedia
import tn.iptv.nextplayer.domain.models.packages.PackageMedia
import tn.iptv.nextplayer.domain.models.packages.ResponsePackage
import tn.iptv.nextplayer.domain.models.packages.ResponsePackageItem
import tn.iptv.nextplayer.domain.models.saisons.ResponseSaisonsOfSerie
import tn.iptv.nextplayer.domain.models.saisons.SaisonItem
import tn.iptv.nextplayer.domain.models.series.ListSeriesByCategory
import tn.iptv.nextplayer.domain.models.series.MediaItem
import java.io.IOException
import java.util.Locale


class ChannelImp(private var application: Application) : ChannelManager {

    private val TAG = "ChannelManager"
    private var job: Job? = null
    private var scopeIO = CoroutineScope(Dispatchers.IO)
    private var scopeMain = CoroutineScope(Dispatchers.Main)


    /**
     * [ChannelManager.channelSelected]
     * */
    override var channelSelected: MutableLiveData<Channel> = MutableLiveData(Channel())


    /**
     * [ChannelManager.listOfFilterSeriesYear]
     * */

    override var listOfFilterSeriesYear: MutableLiveData<MutableList<String>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfFilterSeriesGenre]
     * */

    override var listOfFilterSeriesGenre: MutableLiveData<MutableList<String>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfFilterMovieYear]
     * */

    override var listOfFilterMovieYear: MutableLiveData<MutableList<String>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfFilterMovieGenre]
     * */

    override var listOfFilterMovieGenre: MutableLiveData<MutableList<String>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.activationCode]
     * */
    override var activationCode: MutableLiveData<String> = MutableLiveData("")


    /**
     * [ChannelManager.searchValue]
     * */
    override var searchValue: MutableLiveData<String> = MutableLiveData("")


    /**
     * [ChannelManager.listOfPackages]
     * */
    override var listOfPackages: MutableLiveData<MutableList<ResponsePackageItem>> = MutableLiveData(ArrayList())

    /**
     * [ChannelManager.listOfChannels]
     * */
    override var listOfChannels: MutableLiveData<MutableList<Channel>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfPackagesOfLiveTV]
     * */
    override var listOfPackagesOfLiveTV: MutableLiveData<MutableList<PackageMedia>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.selectedPackageOfLiveTV]
     * */
    override var selectedPackageOfLiveTV: MutableLiveData<PackageMedia> = MutableLiveData(PackageMedia())


    /**
     * [ChannelManager.listOfFavorites]
     * */

    override var listOfFavorites: MutableLiveData<MutableList<Favorite>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfPackagesOfSeries]
     * */
    override var listOfPackagesOfSeries: MutableLiveData<MutableList<PackageMedia>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listOfPackagesOfMovies]
     * */
    override var listOfPackagesOfMovies: MutableLiveData<MutableList<PackageMedia>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.selectedPackageOfMovies]
     * */
    override var selectedPackageOfMovies: MutableLiveData<PackageMedia> = MutableLiveData(PackageMedia())

    /**
     * [ChannelManager.movieSelected]
     * */
    override var movieSelected: MutableLiveData<MediaItem> = MutableLiveData(MediaItem())


    /**
     * [ChannelManager.selectedPackageOfSeries]
     * */
    override var selectedPackageOfSeries: MutableLiveData<PackageMedia> = MutableLiveData(PackageMedia())


    /**
     * [ChannelManager.selectedEpisodeToWatch]
     * */
    override var selectedEpisodeToWatch: MutableLiveData<EpisodeItem> = MutableLiveData(EpisodeItem())


    /**
     * [ChannelManager.seriesIsLoading]
     * */
    override var seriesIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * [ChannelManager.homeIsLoading]
     * */
    override var homeIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    /**
     * [ChannelManager.favoriteIsLoading]
     * */
    override var favoriteIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * [ChannelManager.moviesIsLoading]
     * */
    override var moviesIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    /**
     * [ChannelManager.liveTVIsLoading]
     * */
    override var liveTVIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    /**
     * [ChannelManager.listGroupedSeriesByCategory]
     * */
    override var listGroupedSeriesByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())

    /**
     * [ChannelManager.allListGroupedSeriesByCategory]
     * */
    override var allListGroupedSeriesByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listGroupedMovieByCategory]
     * */
    override var listGroupedMovieByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())

    /**
     * [ChannelManager.allListGroupedMovieByCategory]
     * */
    override var allListGroupedMovieByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listGroupedLiveTVByCategory]
     * */
    override var listGroupedLiveTVByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())

    /**
     * [ChannelManager.allListGroupedLiveTVByCategory]
     * */
    override var allListGroupedLiveTVByCategory: MutableLiveData<MutableList<GroupedMedia>> = MutableLiveData(ArrayList())

    /**
     * [ChannelManager.liveSelected]
     * */
    override var liveSelected: MutableLiveData<PackageMedia> = MutableLiveData(PackageMedia())

    /**
     * [ChannelManager.homeSelected]
     * */
    override var homeSelected: MutableLiveData<CategoryMedia> =
        MutableLiveData(CategoryMedia())

    /**
     * [ChannelManager.serieSelected]
     * */
    override var serieSelected: MutableLiveData<MediaItem> = MutableLiveData(MediaItem())


    /**
     * [ChannelManager.listOfSaisonBySerie]
     * */
    override var listOfSaisonBySerie: MutableLiveData<MutableList<SaisonItem>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.listGroupedEpisodeBySaison]
     * */
    override var listGroupedEpisodeBySaison: MutableLiveData<MutableList<GroupedEpisode>> = MutableLiveData(ArrayList())


    /**
     * [ChannelManager.fetchListChannels]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchListChannels() {
        Log.d(TAG, "fetchListChannels")
        job?.cancel()
        job = scopeMain.launch {
            val response = try {
                RetrofitInstance.getChannelsApi().getChannel()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, " isSuccessful --> ${response.body()!!}")
                listOfChannels.postValue(response.body()!!.data.toMutableList())
            } else {
                Log.e(TAG, "Response not successful")
            }
        }

    }


    /**
     * [ChannelManager.newLoginToChannel]
     * */
    @SuppressLint("LogNotTimber")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun newLoginToChannel(
        loginModel: LoginModel,
        fullUrl: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        job?.cancel()
        job = scopeMain.launch {

            val code = loginModel.code
            val mac = loginModel.mac
            val sn = loginModel.sn
            val model = loginModel.model


            Log.d("LoginHichem", "code:$code")
            Log.d("LoginHichem", "mac:$mac")
            Log.d("LoginHichem", "sn:$sn")
            Log.d("LoginHichem", "model:$model")

            val response = try {

                Log.d("newLoginToChannel", "changeBaseUrl   $fullUrl/login/")
                RetrofitInstance.changeBaseUrl("$fullUrl/login/")
                RetrofitInstance.getChannelsApi().loginChannelSHahidGtv(code, mac, sn, model).awaitResponse()
            } catch (e: IOException) {
                Log.e("newLoginToChannel", "IOException $e")
                return@launch
            } catch (e: HttpException) {
                Log.e("newLoginToChannel", "HttpException  $e")
                return@launch
            }

            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string()

                    try {
                        val jsonObject = JSONObject(xorString(decryptedResponse, cryptKey))
                        Log.i("newLoginToChannel", "Reply Data  ${jsonObject} ")
                        val status = jsonObject.getString("status")
                        if (status == "0") onFailure()
                        else onSuccess()

                        activationCode.value = code

                    } catch (jsonException: Exception) {
                        onFailure()
                        Log.e("newLoginToChannel ", "JSON Error  $jsonException")
                    }
                }
            } else {
                onFailure()
                Log.e("newLoginToChannel", " Response Error Code: ${response.code()} Message: ${response.message()}")
            }
        }

    }


    /**
     * [ChannelManager.restoreAccount]
     * */
    @SuppressLint("LogNotTimber")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun restoreAccount(
        mac: String,
        sn: String,
        fullUrl: String,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit,
    ) {
        job?.cancel()
        job = scopeMain.launch {


            val response = try {
                val hash: String = AES256Cipher.md5("${mac}GltvProMax25${sn}")
                RetrofitInstance.changeBaseUrl("$fullUrl/login/")
                RetrofitInstance.getChannelsApi().restoreAccount(mac, sn, hash).awaitResponse()
            } catch (e: IOException) {
                Log.e("restoreAccount", "IOException $e")
                return@launch
            } catch (e: HttpException) {
                Log.e("restoreAccount", "HttpException  $e")
                return@launch
            }

            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string()


                    try {
                        val jsonObject = JSONObject(xorString(decryptedResponse, cryptKey))
                        val status = jsonObject.getString("status")
                        if (status == "1") {

                            onSuccess(jsonObject.getString("code"))
                        } else onFailure()


                    } catch (jsonException: Exception) {
                        // onFailure()
                        Log.e("restoreAccount ", "JSON Error  $jsonException")
                    }
                }
            } else {
                onFailure()
                Log.e("restoreAccount", " Response Error Code: ${response.code()} Message: ${response.message()}")
            }
        }

    }


    /**
     * [ChannelManager.fetchPackages]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchPackages() {
        Log.d(TAG, "fetchPackages")
        job?.cancel()
        job = scopeMain.launch {
            homeIsLoading.postValue(true)
            val response = try {

                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/")
                RetrofitInstance.getChannelsApi().getPackages(activationCode.value!!).awaitResponse()
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    listOfPackages.value?.clear()
                    val decryptedResponse = responseBody.string()

                    val jsonStringResult = xorString(decryptedResponse, cryptKey)

                    val gson = Gson()
                    val modelResponsePackage = gson.fromJson(jsonStringResult, ResponsePackage::class.java)

                    parsePackages(modelResponsePackage)

                    homeIsLoading.postValue(false)

                }
            }
        }

    }


    /**
     * [ChannelManager.fetchFavorites]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchFavorites(favoriteViewModel: FavoriteViewModel) {
        Log.d(TAG, "fetchFavorites")
        job?.cancel()
        job = scopeMain.launch {
            listOfFavorites.value?.clear()
            favoriteIsLoading.postValue(true)
            Log.d("kkkkkkkkkkkkkkkkkkkk", "kkkkkkkkkkkkkkkk")

            favoriteViewModel.getAllFavorite().map { favorites ->
                listOfFavorites.value?.add(favorites)
            }
            Log.d("kkkkkkkkkkkkkkkkkkkk", "kkkkkkkkkkkkkkkk")

            Log.d("TestOmran", listOfFavorites.value?.size.toString())

            favoriteIsLoading.postValue(false)
        }

    }


    /**
     * [ChannelManager.fetchCategorySeriesAndSeries]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchCategorySeriesAndSeries(searchValue: String) {
        Log.d(TAG, "fetchCategorySeries")
        job?.cancel()
        job = scopeMain.launch {
            seriesIsLoading.postValue(true)
            val response = try {

                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/serie/")
                Log.d("fetchCategorySeries", " selectedPackageOfSeries  ${selectedPackageOfSeries.value!!.id} ")
                RetrofitInstance.getChannelsApi().getCategoriesSeries(id = selectedPackageOfSeries.value!!.id    /*  selectedPackageOfSeries.value!!.id    8740 */, code = activationCode.value!!).awaitResponse()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            // Vérification de la réponse
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString
                    Log.i("fetchCategorySeries", "Reply Data  $decryptedResponse ")
                    Log.i("fetchCategorySeries", "Reply Data decrypted ${xorString(decryptedResponse, cryptKey)} ")

                    val jsonStringResult = xorString(decryptedResponse, cryptKey)


                    val gson = Gson()
                    val modelResponseCategory = gson.fromJson(jsonStringResult, ResponseCategory::class.java)
                    Log.d("fetchCategorySeries", "modelResponsePackage ${modelResponseCategory.toString()} ")


                    val listGroupedSeries = ArrayList<GroupedMedia>()

                    listOfFilterMovieYear.value?.clear()
                    listOfFilterMovieGenre.value?.clear()


                    val listYear = ArrayList<String>()
                    val listGenre = ArrayList<String>()





                    modelResponseCategory.forEach { cat ->
                        Log.d("fetchCategorySeries", "  ${cat.id}  _  ${cat.name}")
                        val responseSeriesByCat = RetrofitInstance.getChannelsApi().getSeriesByCategory(cat.id.toInt(), activationCode.value!!).awaitResponse()


                        if (responseSeriesByCat.isSuccessful) {
                            responseSeriesByCat.body()?.let { responseBody ->
                                val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString

                                val jsonStringResult = xorString(decryptedResponse, cryptKey).trimIndent()
                                Log.i("fetchSeriesByCategory", "Reply Data  $jsonStringResult ")

                                val gson = Gson()
                                val modelResponseSeriesByCat = gson.fromJson(jsonStringResult, ListSeriesByCategory::class.java)
                                Log.e("fetchSeriesByCategory", "modelResponseSeriesByCat ${modelResponseSeriesByCat.toString()} ")

                                val genreSeries = GroupedMedia(cat.name, listSeries = modelResponseSeriesByCat)
                                listGroupedSeries.add(genreSeries)
                                listGroupedSeries.forEach { item ->
                                    item.listSeries.forEach { mediaItem ->
                                        if (mediaItem.date.isNotEmpty())
                                            listYear.add(mediaItem.date.split("-")[0])
                                        if (mediaItem.genre.isNotEmpty()) {
                                            listGenre.add(mediaItem.genre)
                                            listGenre.toSet().toList()
                                        }

                                    }
                                }

                            }
                        }
                    }

                    val genres = processGenres(listGenre)

                    listOfFilterSeriesYear.value?.addAll(listYear.distinct().sortedByDescending { it })
                    listOfFilterSeriesGenre.value?.addAll(genres.distinct().sortedByDescending { it })
                    listOfFilterSeriesYear.value!!.add(0, "Tous")
                    listOfFilterSeriesGenre.value!!.add(0, "Tous")



                    allListGroupedSeriesByCategory.value = listGroupedSeries

                    if (searchValue.isEmpty()) {
                        listGroupedSeriesByCategory.value = listGroupedSeries

                    } else listGroupedSeriesByCategory.value = filterListByName(listGroupedSeries, searchValue)

                    seriesIsLoading.postValue(false)

                }
            }
        }

    }

    /**
     * [ChannelManager.searchCategorySeriesAndSeries]
     * */
    override fun searchCategorySeriesAndSeries(searchValue: String) {
        if (searchValue.isEmpty()) {
            listGroupedSeriesByCategory.value = allListGroupedSeriesByCategory.value
        } else listGroupedSeriesByCategory.value = allListGroupedSeriesByCategory.value?.toCollection(ArrayList())?.let { filterListByName(it, searchValue) }

    }

    /**
     * [ChannelManager.fetchCategoryMoviesAndMovies]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchCategoryMoviesAndMovies(searchValue: String) {
        Log.d(TAG, "fetchCategoryMovies")
        job?.cancel()
        job = scopeMain.launch {
            moviesIsLoading.postValue(true)
            val response = try {

                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/movie/")
                Log.d("fetchCategorySeries", " selectedPackageOfSeries  ${selectedPackageOfMovies.value!!.id} ")  //8740
                RetrofitInstance.getChannelsApi().getCategoriesMovies(id = selectedPackageOfMovies.value!!.id, code = activationCode.value!!).awaitResponse()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            // Vérification de la réponse
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString
                    Log.i("fetchCategoryMovies", "Reply Data  $decryptedResponse ")
                    Log.i("fetchCategoryMovies", "Reply Data decrypted ${xorString(decryptedResponse, cryptKey)} ")

                    val jsonStringResult = xorString(decryptedResponse, cryptKey)

                    val gson = Gson()
                    val modelResponseCategory = gson.fromJson(jsonStringResult, ResponseCategory::class.java)
                    Log.d("fetchCategoryMovies", "modelResponsePackage ${modelResponseCategory.toString()} ")
                    listOfFilterMovieYear.value?.clear()
                    listOfFilterMovieGenre.value?.clear()

                    val listGroupedMovies = ArrayList<GroupedMedia>()

                    val listYear = ArrayList<String>()
                    val listGenre = ArrayList<String>()


                    modelResponseCategory.forEach { cat ->
                        Log.d("fetchCategoryMovies", "  ${cat.id}  _  ${cat.name} _  ${cat.lang}")
                        val responseMoviesByCat = RetrofitInstance.getChannelsApi().getMoviesByCategory(cat.id.toInt(), activationCode.value!!).awaitResponse()


                        if (responseMoviesByCat.isSuccessful) {
                            responseMoviesByCat.body()?.let { responseBody ->
                                val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString

                                val jsonStringResult = xorString(decryptedResponse, cryptKey).trimIndent()
                                Log.i("fetchCategoryMovies", "Reply Data  $jsonStringResult ")

                                val gson = Gson()
                                val modelResponseMoviesByCat = gson.fromJson(jsonStringResult, ListSeriesByCategory::class.java)
                                Log.e("fetchCategoryMovies", "modelResponseMoviesByCat ${modelResponseMoviesByCat.toString()} ")

                                val genreSeries = GroupedMedia(cat.name, listSeries = modelResponseMoviesByCat)
                                listGroupedMovies.add(genreSeries)

                                listGroupedMovies.forEach { item ->
                                    item.listSeries.forEach { mediaItem ->
                                        if (mediaItem.date.isNotEmpty())
                                            listYear.add(mediaItem.date.split("-")[0])
                                        if (mediaItem.genre.isNotEmpty()) {
                                            listGenre.add(mediaItem.genre)
                                            listGenre.toSet().toList()
                                        }

                                    }
                                }

                            }
                        }

                    }
                    val genres = processGenres(listGenre)




                    listOfFilterMovieYear.value?.addAll(listYear.distinct().sortedByDescending { it })
                    listOfFilterMovieGenre.value?.addAll(genres.distinct().sortedByDescending { it })
                    listOfFilterMovieGenre.value!!.add(0, "Tous")
                    listOfFilterMovieYear.value!!.add(0, "Tous")


                    allListGroupedMovieByCategory.value = listGroupedMovies

                    if (searchValue.isEmpty()) {
                        listGroupedMovieByCategory.value = listGroupedMovies
                    } else listGroupedMovieByCategory.value = filterListByName(listGroupedMovies, searchValue)


                    moviesIsLoading.postValue(false)

                }
            }
        }

    }

    fun processGenres(genres: List<String>): List<String> {
        return genres
            .flatMap { it.split(",") } // Divise chaque chaîne en plusieurs genres
            .map { it.trim() }         // Supprime les espaces autour de chaque genre
            .filter { it.isNotEmpty() } // Retire les genres vides
            .toSet()                   // Supprime les doublons
            .toList()                  // Convertit en liste
            .sorted()                  // (Facultatif) Trie les genres
    }


    /**
     * [ChannelManager.fetchCategoryMoviesAndMovies]
     * */
    override fun searchCategoryMoviesAndMovies(searchValue: String) {


        if (searchValue.isEmpty()) {
            listGroupedMovieByCategory.value = allListGroupedMovieByCategory.value
        } else listGroupedMovieByCategory.value = allListGroupedMovieByCategory.value?.toCollection(ArrayList())?.let { filterListByName(it, searchValue) }
    }


    /**
     * [ChannelManager.fetchCategoryLiveTVAndLiveTV]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchCategoryLiveTVAndLiveTV(searchValue: String) {

        Log.d(TAG, "fetchCategoryLiveTV")
        job?.cancel()
        job = scopeMain.launch {
            liveTVIsLoading.postValue(true)
            val response = try {

                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/live/")
                Log.d("fetchCategoryLiveTV", " fetchCategoryLiveTV   ${selectedPackageOfLiveTV.value!!.id} ")
                RetrofitInstance.getChannelsApi().getCategoriesLiveTV(id = selectedPackageOfLiveTV.value!!.id, code = activationCode.value!!).awaitResponse()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            // Vérification de la réponse
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString
                    Log.i("fetchCategoryLiveTV", "Reply Data  $decryptedResponse ")
                    Log.i("fetchCategoryLiveTV", "Reply Data decrypted ${xorString(decryptedResponse, cryptKey)} ")

                    val jsonStringResult = xorString(decryptedResponse, cryptKey)

                    val gson = Gson()
                    val modelResponseCategory = gson.fromJson(jsonStringResult, ResponseCategory::class.java)
                    Log.d("fetchCategoryLiveTV", "modelResponsePackage ${modelResponseCategory.toString()} ")


                    //////////////////////////////////////////////


                    val listGroupedLiveTV = ArrayList<GroupedMedia>()

                    modelResponseCategory.forEach { cat ->
                        Log.d("fetchCategoryLiveTV", "  ${cat.id}  _  ${cat.name}")
                        val responseMoviesByCat = RetrofitInstance.getChannelsApi().getLiveChannelByCategory(cat.id.toInt(), activationCode.value!!).awaitResponse()
                        if (responseMoviesByCat.isSuccessful) {
                            responseMoviesByCat.body()?.let { responseBody ->
                                val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString

                                val jsonStringResult = xorString(decryptedResponse, cryptKey).trimIndent()
                                Log.i("fetchCategoryLiveTV", "Reply Data  $jsonStringResult ")

                                val gson = Gson()
                                val modelResponseMoviesByCat = gson.fromJson(jsonStringResult, ListSeriesByCategory::class.java)
                                Log.e("fetchCategoryLiveTV", "modelResponseLiveTVByCat ${modelResponseMoviesByCat.toString()} ")

                                val genreSeries = GroupedMedia(cat.name, listSeries = modelResponseMoviesByCat)
                                listGroupedLiveTV.add(genreSeries)

                            }
                        }

                    }

                    Log.e("fetchCategoryLiveTV", "modelResponseLiveTVByCat  searchValue $searchValue ")
                    allListGroupedLiveTVByCategory.value = listGroupedLiveTV

                    if (searchValue.isEmpty()) {
                        listGroupedLiveTVByCategory.value = listGroupedLiveTV
                    } else listGroupedLiveTVByCategory.value = filterListByName(listGroupedLiveTV, searchValue)


                    ///////////////////////////////////////////////////////////////////////////////////////////////////////

                    liveTVIsLoading.postValue(false)

                }
            }
        }

    }


    /**
     * [ChannelManager.fetchCategoryLiveTVAndLiveTV]
     * */
    override fun searchForCategoryLiveTVAndLiveTV(searchValue: String) {


        if (searchValue.isEmpty()) {
            listGroupedLiveTVByCategory.value = allListGroupedLiveTVByCategory.value
        } else listGroupedLiveTVByCategory.value = allListGroupedLiveTVByCategory.value?.toCollection(ArrayList())?.let { filterListByName(it, searchValue) }
    }


    private fun filterListByName(listGroupedLiveTV: ArrayList<GroupedMedia>, searchValue: String): ArrayList<GroupedMedia> {

        val newArray = ArrayList<GroupedMedia>()
        listGroupedLiveTV.forEach { grpTV ->

            val newArrayMediaItem: ArrayList<MediaItem> = ArrayList<MediaItem>()

            grpTV.listSeries.forEach { mediaItem ->
                if (mediaItem.name.lowercase(Locale.getDefault()).contains(searchValue.lowercase(Locale.getDefault())))

                    newArrayMediaItem.add(mediaItem)
            }
            if (newArrayMediaItem.isNotEmpty()) {
                val genreSeries = GroupedMedia(grpTV.labelGenre, listSeries = newArrayMediaItem)
                newArray.add(genreSeries)
            }
        }
        Log.e("dashBord", "searchValue $searchValue  ---- newArray  ${newArray.size}")
        return newArray
    }


    /**
     * [ChannelManager.fetchSaisonBySerie]
     * */
    @SuppressLint("LogNotTimber")
    override fun fetchSaisonBySerie() {
        Log.d(TAG, "fetchSaisonBySerie")
        job?.cancel()
        job = scopeMain.launch {
            val responseSai = try {

                Log.e("fetchSaisonBySerie", " url " + channelSelected.value!!.url + "/serie/   serieSelected " + serieSelected.value!!.id)
                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/serie/")

                RetrofitInstance.getChannelsApi().getSaisonsBySerie(serieSelected.value!!.id, activationCode.value!!).awaitResponse()

            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }

            val listGroupedEpisode = ArrayList<GroupedEpisode>()
            // Vérification de la réponse
            if (responseSai.isSuccessful) {
                responseSai.body()?.let { responseBodySai ->
                    val decryptedResponseSai = responseBodySai.string()
                    val jsonStringResultSai = xorString(decryptedResponseSai, cryptKey).trimIndent()
                    Log.i("fetchSaisonBySerie", "Reply Data  $jsonStringResultSai ")

                    val gson = Gson()
                    val listSaisonOfSerie = gson.fromJson(jsonStringResultSai, ResponseSaisonsOfSerie::class.java)
                    Log.i("fetchSaisonBySerie", "listSaisonOfSerie ${listSaisonOfSerie.size}  ${listSaisonOfSerie.toString()}")


                    listSaisonOfSerie.forEach { saison ->

                        val responseListEpisodeBySaison = RetrofitInstance.getChannelsApi().getEpisodesBySerieAndSaison(
                            id = serieSelected.value!!.id,
                            code = activationCode.value!!,
                            saison = "${saison.saison}",
                        ).awaitResponse()

                        if (responseListEpisodeBySaison.isSuccessful) {
                            responseListEpisodeBySaison.body()?.let { responseEp ->

                                val decryptedResponseEp = responseEp.string()
                                val jsonStringResultEp = xorString(decryptedResponseEp, cryptKey).trimIndent()
                                val listEpisodeBySaison = gson.fromJson(jsonStringResultEp, ResponseListEpisode::class.java)
                                Log.d("fetchSaisonBySerie", "episode ---  $listEpisodeBySaison")
                                listGroupedEpisode.add(GroupedEpisode("Season ${saison.saison}", listEpisodeBySaison))
                            }
                        }
                    }


                    listGroupedEpisodeBySaison.value = listGroupedEpisode
                    listOfSaisonBySerie.value = listSaisonOfSerie

                    Log.d("fetchSaisonBySerie", "episode ---  ${listGroupedEpisodeBySaison.value}")

                }
            }
        }
    }


    /**
     * [ChannelManager.getCategoryLiveByGroupID]
     * */
    override fun getCategoryLiveByGroupID() {

        Log.d(TAG, "fetchPackages")
        job?.cancel()
        job = scopeMain.launch {
            val response = try {

                RetrofitInstance.changeBaseUrl(channelSelected.value!!.url + "/live/")
                //RetrofitInstance.changeBaseUrl("http://onplus.me:6739/api-GlobaLtv/")
                RetrofitInstance.getChannelsApi().getCategoriesChannelsByParentID(activationCode.value!!, liveSelected.value!!.id.toString()).awaitResponse()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            // Vérification de la réponse
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val decryptedResponse = responseBody.string() // Si vous devez déchiffrer, utilisez xorString
                    Log.i("getCategoryLivByGroupID", "Reply Data  $decryptedResponse ")
                    Log.i("getCategoryLivByGroupID", "Reply Data  ${xorString(decryptedResponse, cryptKey)} ")

                    val jsonStringResult = xorString(decryptedResponse, cryptKey)

                    /*      val gson = Gson()
                          val modelResponsePackage = gson.fromJson(jsonStringResult, ResponsePackage::class.java)
                          Log.d("fetchListLiveChannel", "modelResponsePackage ${modelResponsePackage.toString()} ")

                          parsePackages(modelResponsePackage)*/

                }
            }
        }


    }


    private fun parsePackages(modelResponsePackage: ResponsePackage?) {

        modelResponsePackage?.forEach { packageItem ->
            listOfPackages.value?.add(packageItem)

            if (packageItem.screen == "1" && packageItem.name == "Live TV") {

                listOfPackagesOfLiveTV.value = packageItem.live.toMutableList()
                selectedPackageOfLiveTV.value = listOfPackagesOfLiveTV.value!!.first()

                runBlocking {
                    searchValue.value?.let { fetchCategoryLiveTVAndLiveTV(it) }
                }


            } else if (packageItem.screen == "2" && packageItem.name == "Movies") {

                listOfPackagesOfMovies.value = packageItem.movies.toMutableList()
                selectedPackageOfMovies.value = listOfPackagesOfMovies.value!!.first()


                runBlocking {
                    searchValue.value?.let { fetchCategoryMoviesAndMovies(it) }
                }


            } else if (packageItem.screen == "3" && packageItem.name == "Series") {

                listOfPackagesOfSeries.value = packageItem.series.toMutableList()
                selectedPackageOfSeries.value = listOfPackagesOfSeries.value!!.first()
                runBlocking {
                    searchValue.value?.let { fetchCategorySeriesAndSeries(it) }
                }

            }

        }

    }


    private fun xorString(input: String, key: String): String {
        val output = StringBuilder()
        for (i in input.indices) {
            val xorChar = input[i].code xor key[i % key.length].code
            output.append(xorChar.toChar())
        }
        return output.toString()
    }
}