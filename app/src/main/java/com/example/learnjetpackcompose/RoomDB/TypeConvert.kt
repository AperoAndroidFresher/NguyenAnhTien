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
}