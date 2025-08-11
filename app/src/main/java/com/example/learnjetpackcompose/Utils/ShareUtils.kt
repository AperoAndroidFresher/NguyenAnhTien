package com.example.learnjetpackcompose.Utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel

object ShareUtils {
    fun shareAudioFile(context: Context, song: Song) {
        try {
            val uri: Uri = when {
                song.data.startsWith("content://") -> Uri.parse(song.data)
                else -> {
                    val sourceFile = File(song.data)
                    if (!sourceFile.exists()) return
                    getSharableUri(context, sourceFile) ?: return
                }
            }

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Sharing: ${song.title}")
                putExtra(Intent.EXTRA_TEXT, "Check out this song: ${song.title} by ${song.artist}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share ${song.title}")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } catch (_: Exception) {

        }
    }

    private fun getSharableUri(context: Context, sourceFile: File): Uri? {
        val filesDirPath = context.filesDir.absolutePath
        val cacheDirPath = context.cacheDir.absolutePath

        val isAppPrivate = sourceFile.absolutePath.startsWith(filesDirPath) ||
                sourceFile.absolutePath.startsWith(cacheDirPath)

        val targetFile: File = if (isAppPrivate) {
            sourceFile
        } else {
            // Copy to cache/share to ensure accessibility across apps
            val shareDir = File(context.cacheDir, "share").apply { mkdirs() }
            val sanitizedName = sanitizeFileName(sourceFile.name)
            val destFile = File(shareDir, sanitizedName)
            copyFile(sourceFile, destFile)
            destFile
        }

        return try {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                targetFile
            )
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    private fun sanitizeFileName(name: String): String {
        val base = name.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return if (base.contains('.')) base else "$base.mp3"
    }

    private fun copyFile(source: File, dest: File) {
        if (!source.exists()) return
        if (dest.exists()) return
        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null
        try {
            inChannel = FileInputStream(source).channel
            outChannel = FileOutputStream(dest).channel
            outChannel.transferFrom(inChannel, 0, inChannel.size())
        } catch (_: Exception) {
        } finally {
            try { inChannel?.close() } catch (_: Exception) {}
            try { outChannel?.close() } catch (_: Exception) {}
        }
    }
}
