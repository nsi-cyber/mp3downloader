package com.nsicyber.mp3downloader.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.nsicyber.mp3downloader.transformToEnglishCompatible
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloaderRepository @Inject constructor() {

    val client = OkHttpClient()

    var totalBytes = MutableLiveData<Long>()
    var downloadedBytes = MutableLiveData<Int>()
    val progress = MutableLiveData<Int>()

    var totalBytesR: Long = 0
    var downloadedBytesR: Int = 0
    var progressR: Int = 0


    suspend fun downloadUrl(url: String, uri: String, title: String) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // İndirme başarısız oldu, hata durumunu yönetin
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // İndirme başarısız oldu, hata durumunu yönetin
                    throw IOException("Unexpected response code: ${response.code}")
                }

                val responseBody: ResponseBody? = response.body
                if (responseBody != null) {


                    totalBytesR = (responseBody.contentLength())
                    downloadedBytesR = (0)
                    totalBytes.postValue(responseBody.contentLength())
                    downloadedBytes.postValue(0)

                    val inputStream = responseBody.byteStream()

                    val directory = File(uri)
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }


                    try {
                        val file = File(directory, "${transformToEnglishCompatible(title)}.mp3")
                        val outputStream = FileOutputStream(file)

                        val buffer = ByteArray(4096)
                        var bytesRead: Int

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            downloadedBytesR = (downloadedBytesR + bytesRead)
                            downloadedBytes.postValue(downloadedBytesR + bytesRead)

                            // İndirme ilerlemesini göstermek için anlık byte bilgilerini kullanabilirsiniz
                            // Örneğin:
                            progressR = (((downloadedBytesR * 100 / totalBytesR)).toInt())
                            progress.postValue(((downloadedBytesR * 100 / totalBytesR)).toInt())
                            println("Download Progress: ${progress.value}%")


                        }

                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                        println(progressR)
                        println(totalBytesR)
                        println(downloadedBytesR)
                        println("Download Complete")
                    } catch (e: Exception) {
                        println(e.toString())
                        println(e.toString())
                    }


                } else {
                    // Yanıt gövdesi boş ise hata durumunu yönetin
                    throw IOException("Response body is null")
                }
            }
        })
    }


}
