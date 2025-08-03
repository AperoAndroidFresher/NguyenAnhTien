package com.example.learnjetpackcompose

import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel

object ViewModelProvider {

    val playlistViewModel by lazy { PlaylistViewModel() }
    val libraryViewModel by lazy { LibraryViewModel() }
}