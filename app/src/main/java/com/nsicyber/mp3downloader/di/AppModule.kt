package com.nsicyber.mp3downloader.di

import android.app.Application
import com.nsicyber.mp3downloader.network.ApiInterface
import com.nsicyber.mp3downloader.Constants.BASE_URL
import com.nsicyber.mp3downloader.ContextProvider
import com.nsicyber.mp3downloader.repositories.DownloaderRepository
import com.nsicyber.mp3downloader.repositories.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContextProvider(application: Application): ContextProvider {
        return AppContextProvider(application)
    }


    @Singleton
    @Provides
    fun provideYoutubeRepository(api: ApiInterface) = YoutubeRepository(api)

    @Singleton
    @Provides
    fun provideYoutubeApi(): ApiInterface {
        return Retrofit.Builder().addConverterFactory((GsonConverterFactory.create()))
            .baseUrl(BASE_URL).build().create(ApiInterface::class.java)
    }

}