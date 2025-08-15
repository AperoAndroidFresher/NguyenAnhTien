import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import java.io.File

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
        val duration = formatTime(durationStr!!.toLong())
        val songId = file.absolutePath.hashCode().toLong()
        val albumArtUri = getAlbumArtUriFromFile(context, file.absolutePath)
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

private fun getAlbumArtUriFromFile(context: Context, filePath: String): Uri? {
    try {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DATA} = ?"
        val selectionArgs = arrayOf(filePath)

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val albumIdIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val albumId = it.getLong(albumIdIndex)
                val mediaStoreUri = getAlbumArtUri(albumId)
                if (mediaStoreUri != null) {
                    return mediaStoreUri
                }
            }
        }
    } catch (e: Exception) {
    }
    return extractAlbumArtFromFile(context, filePath)
}

private fun extractAlbumArtFromFile(context: Context, filePath: String): Uri? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(filePath)
        val artBytes = retriever.embeddedPicture

        if (artBytes != null) {
            val cacheDir = File(context.cacheDir, "album_arts")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val songId = filePath.hashCode().toLong().toString()
            val tempFile = File(cacheDir, "art_$songId.jpg")

            tempFile.writeBytes(artBytes)
            Uri.fromFile(tempFile)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    } finally {
        try {
            retriever.release()
        } catch (e: Exception) {
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / 1000) / 60
    return String.format("%d:%02d", minutes, seconds)
}

fun getAllMp3FilesOptimized(context: Context): List<Song> {
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
            val durationInSec = it.getLong(durationIndex)
            val duration = formatTime(durationInSec)

            val albumArtUri = extractAlbumArtFromFile(context, data) ?: Uri.EMPTY

            if (data.endsWith(".mp3", ignoreCase = true)) {
                songList.add(Song(id, title, artist, albumArtUri.toString(), duration, data))
            }
        }
    }

    return songList
}

private fun getQueryProjection(): Array<String> {
    return arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID
    )
}

private fun getAlbumArtUri(albumId: Long): Uri {
    return Uri.parse("content://media/external/audio/albumart/$albumId")
}