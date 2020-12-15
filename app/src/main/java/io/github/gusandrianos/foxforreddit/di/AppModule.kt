package io.github.gusandrianos.foxforreddit.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.gusandrianos.foxforreddit.data.db.FoxDatabase
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(
            application,
            FoxDatabase::class.java,
            "fox_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTokenDao(database: FoxDatabase) =
        database.tokenDao()

    @Provides
    fun provideTokenRepository(): TokenRepository =
        TokenRepository.getInstance()

}
