package com.alienspace.moviedb.di

import android.app.Application
import androidx.room.Room
import com.alienspace.moviedb.movielist.data.local.MovieDatabase
import com.alienspace.moviedb.movielist.data.remote.MovieAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun provideMovieApi(): MovieAPI {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(MovieAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun ProvideMovieDatabase(app:Application):MovieDatabase{
        return Room.databaseBuilder(
            app,MovieDatabase::class.java,"moviedb.db"
        ).also {
            println("Database Created...")
        }.build()
    }
}