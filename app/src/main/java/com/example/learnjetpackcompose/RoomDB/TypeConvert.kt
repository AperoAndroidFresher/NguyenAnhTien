package com.example.learnjetpackcompose.RoomDB

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromSongList(songs: List<Song>): String {
        return gson.toJson(songs)
    }

    @TypeConverter
    fun toSongList(songsJson: String): List<Song> {
        val listType = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(songsJson, listType)
    }

    // Chuyển đổi Bitmap thành mảng byte
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // Chuyển đổi mảng byte thành Bitmap
    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null) return null
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}