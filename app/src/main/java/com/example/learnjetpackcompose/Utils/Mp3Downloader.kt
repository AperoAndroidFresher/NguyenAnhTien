package com.example.learnjetpackcompose.Utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Mp3Downloader(@ApplicationContext internal val context: Context) {

    private val client = OkHttpClient()

    suspend fun download(
        remoteUrl: String,
        rawFileName: String
    ): File = withContext(Dispatchers.IO) {

        val songsDir = File(context.filesDir, "songs")
        if (!songsDir.exists()) songsDir.mkdirs()

        val safeName = rawFileName
            .replace(Regex("[^a-zA-Z0-9._-]"), "_")
            .takeIf { it.isNotBlank() } ?: "track"

        val destFile = File(songsDir,"$safeName.mp3").apply {
            if (exists()) return@withContext this
        }

        val request = Request.Builder().url(remoteUrl).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)
                throw Exception("Download failed: ${response.code}")
            response.body?.byteStream()?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return@withContext destFile
    }
}
///data/user/0/com.example.learnjetpackcompose/files/songs