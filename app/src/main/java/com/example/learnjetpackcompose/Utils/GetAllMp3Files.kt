import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import java.io.File

fun getAllMp3Files(context: Context): List<Song> {
    val songList = mutableListOf<Song>()
    val projection = getQueryProjection()
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )

    cursor?.use {
        val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

        while (it.moveToNext()) {
            val id = it.getLong(idIndex)
            val title = it.getString(titleIndex)
            val artist = it.getString(artistIndex)
            val data = it.getString(dataIndex)
            val durationInSec = it.getLong(durationIndex) / 1000
            val minutes = durationInSec / 60
            val seconds = durationInSec % 60
            val duration = String.format("%d:%02d", minutes, seconds)

            val albumArtUri = extractAlbumArtAsUri(context, data, id)
            val finalAlbumArtUri = albumArtUri ?: Uri.EMPTY

            if (data.endsWith(".mp3", ignoreCase = true)) {
                songList.add(Song(id, title, artist, finalAlbumArtUri.toString(), duration, data))
            }
        }
    }

    return songList
}

fun getOfflineRemoteSongs(context: Context): List<Song> {
    val songList = mutableListOf<Song>()

    try {
        val songsDir = File(context.filesDir, "songs")

        if (!songsDir.exists() || !songsDir.isDirectory) {
            return emptyList()
        }

        val mp3Files = songsDir.listFiles { file ->
            file.isFile && file.name.endsWith(".mp3", ignoreCase = true)
        }

        mp3Files?.forEach { file ->
            try {
                val song = createSongFromFile(context, file)
                if (song != null) {
                    songList.add(song)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return songList.sortedBy { it.title }
}

private fun createSongFromFile(context: Context, file: File): Song? {
    val retriever = MediaMetadataRetriever()

    return try {
        retriever.setDataSource(file.absolutePath)

        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            ?: file.nameWithoutExtension
        val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            ?: "Unknown Artist"
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        val duration = formatDuration(durationStr.toString())

        val songId = file.absolutePath.hashCode().toLong()

        val albumArtUri = extractAlbumArtAsUri(context, file.absolutePath, songId)
        val finalAlbumArtUri = albumArtUri ?: Uri.EMPTY

        Song(
            songId = songId,
            title = title,
            artist = artist,
            albumArt = finalAlbumArtUri.toString(),
            duration = duration,
            data = file.absolutePath
        )

    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        try {
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun extractAlbumArtAsUri(context: Context, audioPath: String, songId: Long): Uri? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(audioPath)
        val artBytes = retriever.embeddedPicture

        if (artBytes != null) {
            val cacheDir = File(context.cacheDir, "temp_album_arts")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val tempFile = File(cacheDir, "temp_art_$songId.jpg")

            tempFile.writeBytes(artBytes)

            Uri.fromFile(tempFile)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        try {
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun getQueryProjection(): Array<String> {
    return arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION
    )
}
private fun formatDuration(durationMs: String): String {
    return try {
        val millis = durationMs.toLong()
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        String.format("%d:%02d", minutes, seconds)
    } catch (e: Exception) {
        "0:00"
    }
}