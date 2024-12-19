package tn.iptv.nextplayer.core.data.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import tn.iptv.nextplayer.core.database.dao.FavoriteDao
import tn.iptv.nextplayer.core.database.entities.FavoriteEntity
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
) {

    suspend fun addFavorite(favoriteEntity: FavoriteEntity) {
        favoriteDao.addFavorite(favoriteEntity)
    }

    suspend fun deleteFavoriteById(itemId: String) {
        favoriteDao.deleteFavoriteById(itemId)
    }

    public fun getAllFavoriteByType(type: String): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavoriteByType(type)
    }

    suspend fun deleteAllFavoriteByType(type: String) {
        favoriteDao.deleteAllFavoriteByType(type)
    }

    suspend fun deleteAllFavorite() {
        favoriteDao.deleteAllFavorite()
    }

    suspend fun getSizeFavorites(type: String): Int {
        return getAllFavoriteByType(type).count()
    }


}
