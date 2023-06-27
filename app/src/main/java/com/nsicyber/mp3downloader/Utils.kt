package com.nsicyber.mp3downloader

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import com.google.gson.Gson
import org.jsoup.nodes.Document
import java.io.File
import java.text.Normalizer
import java.util.Objects

fun getLastDirectoryName(uri: String): String {

    // Uri'yi "/" karakterine göre böleriz
    val splittedUri = removePrimary3A(uri).split("/")

    // Bölünen dizinin son elemanını döndürürüz
    return splittedUri.last()
}

fun removePrimary3A(input: String): String {
    val targetString = "primary%3A"

    if (input.contains(targetString)) {
        val startIndex = input.indexOf(targetString)
        val endIndex = startIndex + targetString.length

        return input.removeRange(startIndex, endIndex)
    }

    return input
}


fun <T> T.toJson(): String? {
    return Gson().toJson(this)
}


fun <T> String.fromJson(type: Class<T>): T? {
    try {
        return Gson().fromJson(this, type);
    } catch (e: Exception) {
        print(e)
    }
    return null;
}



fun findCount(input: String,find:String): Int? {
    val pattern = """(?:"${find}"\s*:\s*")(\d+)""".toRegex()
    val matchResult = pattern.find(input)
    return matchResult?.groupValues?.get(1)?.toInt()
}


fun extractVideoIdFromUrl(url: String): String? {
    val regex = Regex("""(?:https?:\/\/)?(?:www\.)?youtu(?:\.be|be\.com)\/(?:watch\?v=|embed\/|v\/|\/embed\/|watch\?v%3D|youtu\.be\/|watch\?.+&v=|watch\?v%3D|youtube.com\/user\/[^#]*#([^\/]*?\/))([^\/\#\?]*).*""")
    val matchResult = regex.find(url)
    return matchResult?.groups?.get(2)?.value
}








fun transformToEnglishCompatible(text: String): String {
    val transformedText = StringBuilder()
    val normalizedText = text.normalize()

    for (i in normalizedText.indices) {
        val char = normalizedText[i]
        when {
            char.isLetterOrDigit() -> transformedText.append(char)
            char.isWhitespace() -> continue
            char == '_' -> transformedText.append('_')
            else -> transformedText.append('.')
        }
    }

    return transformedText.toString()
}

private fun String.normalize(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[^\\p{ASCII}]".toRegex(), "")
}

fun formatBytesToMb(bytes: Int): String {
    val megabytes = bytes.toDouble() / (1024 * 1024)
    return "%.1f Mb".format(megabytes)
}

fun formatNumber(number: Int): String {
    val suffixes = listOf("K", "M", "B", "T", "KT")
    val magnitude = (Math.log10(number.toDouble()) / 3).toInt()

    return if (magnitude > 0) {
        val divisor = Math.pow(10.0, (magnitude * 3).toDouble()).toLong()
        val truncated = number.toLong() / divisor.toDouble()
        "%.1f %s".format(truncated, suffixes[magnitude - 1])
    } else {
        number.toString()
    }
}


fun getRealPathFromURI(uri: Uri?): String? {
    val paths: List<String>? = Objects.requireNonNull(uri?.path)?.split(":")

    return  Environment.getExternalStorageDirectory().toString() +
            if (paths?.size!! > 1) File.separator + (paths.get(1)  ) else ""


}