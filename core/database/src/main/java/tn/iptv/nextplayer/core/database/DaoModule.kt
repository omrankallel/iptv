package tn.iptv.nextplayer.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tn.iptv.nextplayer.core.database.dao.DirectoryDao
import tn.iptv.nextplayer.core.database.dao.FavoriteDao
import tn.iptv.nextplayer.core.database.dao.MediumDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideMediumDao(db: MediaDatabase): MediumDao = db.mediumDao()

    @Provides
    fun provideDirectoryDao(db: MediaDatabase): DirectoryDao = db.directoryDao()

    @Provides
    fun provideFavoriteDao(db: MediaDatabase): FavoriteDao = db.favoriteDao()
}
