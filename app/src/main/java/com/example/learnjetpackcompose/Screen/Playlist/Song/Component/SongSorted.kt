package com.example.learnjetpackcompose.Screen.Playlist.Song.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.SongInfo
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@Composable
fun SongSorted(
    playlistId: Int,
    songs: List<Song>,
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
){
    val playlistViewModel: PlaylistViewModel = hiltViewModel()
    val items = remember(songs) { mutableStateListOf<Song>().apply { addAll(songs) } }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val itemHeightDp = 72.dp
    val itemHeightPx = with(LocalDensity.current) { itemHeightDp.toPx() }
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var startIndex by remember { mutableStateOf<Int?>(null) }
    var draggedOffset by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        HeaderSorted(
            onBackClick = onBackClick,
            onSaveClick = {
                playlistViewModel.processIntent(
                    PlaylistIntent.ReorderSongsInPlaylist(playlistId, items.toList())
                )
                onSaved()
            }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items, key = { _, it -> it.songId }) { index, song ->
                val isDragging = draggingIndex == index
                SongRowSortable(
                    song = song,
                    isDragging = isDragging,
                    modifier = Modifier
                        .pointerInput(index) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    draggingIndex = index
                                    startIndex = index
                                    draggedOffset = 0f
                                },
                                onDragEnd = {
                                    draggingIndex = null
                                    startIndex = null
                                    draggedOffset = 0f
                                },
                                onDragCancel = {
                                    draggingIndex = null
                                    startIndex = null
                                    draggedOffset = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                draggedOffset += dragAmount.y

                                if (draggingIndex != null) {
                                    val first = listState.firstVisibleItemIndex
                                    val last = first + listState.layoutInfo.visibleItemsInfo.size - 1
                                    if (draggingIndex!! <= first + 1 && dragAmount.y < 0) {
                                        scope.launch { listState.scrollBy(dragAmount.y) }
                                    } else if (draggingIndex!! >= last - 1 && dragAmount.y > 0) {
                                        scope.launch { listState.scrollBy(dragAmount.y) }
                                    }
                                }

                                if (startIndex != null && draggingIndex != null) {
                                    val shift = (draggedOffset / itemHeightPx).toInt()
                                    val target = (startIndex!! + shift).coerceIn(0, items.lastIndex)
                                    if (target != draggingIndex) {
                                        val from = draggingIndex!!
                                        val moved = items.removeAt(from)
                                        items.add(target, moved)
                                        draggingIndex = target
                                    }
                                }
                            }
                        }
                        .graphicsLayer {
                            if (isDragging && startIndex != null && draggingIndex != null) {
                                val shift = (draggedOffset - (draggingIndex!! - startIndex!!) * itemHeightPx)
                                translationY = shift
                                shadowElevation = 12f
                                scaleX = 1.02f
                                scaleY = 1.02f
                            }
                        }

                )
            }
        }
    }
}

@Composable
private fun SongRowSortable(song: Song, isDragging: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDragging) Color.DarkGray else Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlbumArt(song.albumArt)
            SongInfo(title = song.title, artist = song.artist)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = song.duration, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            IconButtonCustom(onClick = {}, icon = R.drawable.menu, title = "Drag")
        }
    }
}

@Preview
@Composable
fun HeaderSorted(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButtonCustom(onBackClick, R.drawable.icon_back, "Back", Color.White)
        Text("Sorting", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontSize = 20.sp)
        IconButtonCustom(onSaveClick, R.drawable.icon_yes, "Save", Color.White)
    }
}