package com.nsicyber.mp3downloader

import android.content.Context
import dagger.Provides

interface ContextProvider {
    fun getContext(): Context
}
