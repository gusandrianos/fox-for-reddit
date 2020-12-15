package io.github.gusandrianos.foxforreddit.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.gusandrianos.foxforreddit.data.db.FoxDatabase
import io.github.gusandrianos.foxforreddit.data.network.OAuthTokenAPI
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
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
    @Singleton
    fun provideRedditAPI(): RedditAPI =
        Retrofit.Builder()
            .baseUrl(RedditAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditAPI::class.java)

    @Provides
    @Singleton
    fun provideOAuthTokenAPI(): OAuthTokenAPI =
        Retrofit.Builder()
            .baseUrl(OAuthTokenAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OAuthTokenAPI::class.java)
}
