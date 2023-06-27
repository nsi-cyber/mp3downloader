package com.nsicyber.mp3downloader

import android.app.Application
import android.content.Context
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Mp3DownloaderApplication : Application(),ContextProvider{
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
    override fun getContext(): Context {
        return this
    }
}