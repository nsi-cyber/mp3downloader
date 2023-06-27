package com.nsicyber.mp3downloader.viewModels

import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nsicyber.mp3downloader.repositories.DownloaderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DownloadScreenViewModel @Inject constructor(
    private val repository: DownloaderRepository
) : ViewModel() {
    val downloadProgress = mutableStateOf<Int?>(0)
    val totalByte = mutableStateOf<Long?>(0)
    val currByte = mutableStateOf<Int?>(0)




    suspend fun startDownload(url:String, uri:String, title:String) {

        repository.progress.observeForever { progress ->
            downloadProgress.value = progress
        }
        repository.downloadedBytes.observeForever { curByte ->
            currByte.value = curByte
        }
        repository.totalBytes.observeForever { total ->
            totalByte.value = total
        }
        repository.downloadUrl(url, uri, title)

    }


}


