package tn.iptv.nextplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tn.iptv.nextplayer.core.database.entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @Upsert
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Upsert
    suspend fun upsertAll(favorites: List<FavoriteEntity>)

    @Query("SELECT * FROM favorite WHERE type = :type")
    fun getAllFavoriteByType(type: String): Flow<List<FavoriteEntity>>


    @Query("DELETE FROM favorite WHERE itemId = :itemId")
    suspend fun deleteFavoriteById(itemId: String)

    @Query("DELETE FROM favorite WHERE type = :type")
    suspend fun deleteAllFavoriteByType(type: String)

    @Query("DELETE FROM favorite")
    suspend fun deleteAllFavorite()
}
