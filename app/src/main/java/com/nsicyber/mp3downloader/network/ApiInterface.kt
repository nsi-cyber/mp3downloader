package com.nsicyber.mp3downloader.network

import com.nsicyber.mp3downloader.models.DownloadModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("dl")
    suspend fun downloadVideo(
        @Query("id") idCode: String?,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String
    ): DownloadModel

}