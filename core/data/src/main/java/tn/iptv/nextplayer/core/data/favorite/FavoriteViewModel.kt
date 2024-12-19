package tn.iptv.nextplayer.core.data.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import tn.iptv.nextplayer.core.database.entities.FavoriteEntity
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    fun addFavorite(itemId: String, type: String) {

        viewModelScope.launch {
            favoriteRepository.addFavorite(FavoriteEntity(itemId, type))
        }
    }

    fun deleteFavoriteById(itemId: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavoriteById(itemId)
        }
    }

    fun getAllFavoriteByType(type: String): Flow<List<FavoriteEntity>> {
        return favoriteRepository.getAllFavoriteByType(type)
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
