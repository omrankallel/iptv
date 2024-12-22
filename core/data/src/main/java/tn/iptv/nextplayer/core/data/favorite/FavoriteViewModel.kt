package tn.iptv.nextplayer.core.data.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tn.iptv.nextplayer.core.data.models.Favorite
import tn.iptv.nextplayer.core.database.entities.FavoriteEntity
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    fun addFavorite(itemId: String, name: String, icon: String, url: String, plot: String, cast: String, genre: String, date: String, duration: String, rate: String, age: String, type: String) {

        viewModelScope.launch {
            favoriteRepository.addFavorite(FavoriteEntity(itemId, name, icon, url, plot, cast, genre, date, duration, rate, age, type))
        }
    }

    fun deleteFavoriteById(itemId: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavoriteById(itemId)
        }
    }

    fun isFavoriteExists(itemId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = favoriteRepository.isFavoriteExists(itemId)
            callback(exists)
        }
    }

    suspend fun getAllFavorite(): List<Favorite> {
        return favoriteRepository.getAllFavorite()
            .map { entityList ->
                entityList.map { entity ->
                    Favorite(itemId = entity.itemId, name = entity.name, icon = entity.icon, url = entity.url, plot = entity.plot, cast = entity.cast, genre = entity.genre, date = entity.date, duration = entity.duration, rate = entity.rate, age = entity.age, type = entity.type)
                }
            }.first()
    }

    suspend fun getAllFavoriteByType(type: String): List<Favorite> {
        return favoriteRepository.getAllFavoriteByType(type)
            .map { entityList ->
                entityList.map { entity ->
                    Favorite(itemId = entity.itemId, name = entity.name, icon = entity.icon, url = entity.url, plot = entity.plot, cast = entity.cast, genre = entity.genre, date = entity.date, duration = entity.duration, rate = entity.rate, age = entity.age, type = entity.type)
                }
            }.first()
    }

    fun deleteAllFavoriteByType(type: String) {
        viewModelScope.launch {
            favoriteRepository.deleteAllFavoriteByType(type)
        }
    }

    fun deleteAllFavorite() {
        viewModelScope.launch {
            favoriteRepository.deleteAllFavorite()
        }
    }

    suspend fun getSizeFavorites(type: String): Int {
        return favoriteRepository.getAllFavoriteByType(type).count()
    }
}
