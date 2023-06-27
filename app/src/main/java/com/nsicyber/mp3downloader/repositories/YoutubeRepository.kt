package com.nsicyber.mp3downloader.repositories

import android.os.StrictMode
import com.nsicyber.mp3downloader.Constants
import com.nsicyber.mp3downloader.extractVideoIdFromUrl
import com.nsicyber.mp3downloader.findCount
import com.nsicyber.mp3downloader.models.DownloadModel
import com.nsicyber.mp3downloader.models.VideoInfo
import com.nsicyber.mp3downloader.network.ApiInterface
import dagger.hilt.android.scopes.ActivityScoped
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import javax.inject.Inject


@ActivityScoped
class YoutubeRepository @Inject constructor(
    private val api: ApiInterface
) {
    suspend fun getVideoInfo(url: String,uri:String?): VideoInfo? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val p: Document = Jsoup.connect(url).get()

        val videoId = p.select("meta[property='og:video:url']").attr("content")
        val thumbnail = p.select("meta[property='og:image']").attr("content")
        val title = p.select("meta[property='og:title']").attr("content")
        val views = findCount(p.toString(), "viewCount")
        val likes = findCount(p.toString(), "likeCount")
        var url = getDownloadInfo(extractVideoIdFromUrl(videoId))?.link

        if (videoId.isNullOrEmpty() || thumbnail.isNullOrEmpty() || title.isNullOrEmpty() || views == null || likes == null || url.isNullOrEmpty()) {
            return null
        }

        return VideoInfo(
            thumbnail, title, views, likes,
            url, uri
        )
    }

    private suspend fun getDownloadInfo(url: String?): DownloadModel? {
        val response = try {
            api.downloadVideo(url, Constants.RapidAPIKey, Constants.RapidAPIHost)
        } catch (e: Exception) {
            println(e.toString())
            return null
        }
        return response
    }
}
