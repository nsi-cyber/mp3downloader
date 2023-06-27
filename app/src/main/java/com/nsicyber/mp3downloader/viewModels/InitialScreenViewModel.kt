package com.nsicyber.mp3downloader.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nsicyber.mp3downloader.extractVideoIdFromUrl
import com.nsicyber.mp3downloader.findCount
import com.nsicyber.mp3downloader.models.VideoInfo
import com.nsicyber.mp3downloader.repositories.YoutubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class InitialScreenViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {
    val selectedFolder =
        mutableStateOf<String?>(Environment.getExternalStorageDirectory().toString() + "/mp3downloader")
    val videoData = mutableStateOf<VideoInfo?>(null)

    val isReceived = mutableStateOf<Boolean?>(true)

    init {
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/mp3downloader")
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    fun openFolderPicker(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        return intent
    }

    suspend fun getVideoInfo(url: String) {
        val videoInfo = repository.getVideoInfo(url,selectedFolder.value)
        videoData.value = videoInfo
        isReceived.value = videoInfo != null
    }
}