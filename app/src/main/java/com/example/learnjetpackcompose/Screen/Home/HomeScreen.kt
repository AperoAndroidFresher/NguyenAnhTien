package com.example.learnjetpackcompose.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.data.api.Album
import com.example.learnjetpackcompose.Screen.Home.Component.AlbumCard
import com.example.learnjetpackcompose.Screen.Home.Component.ArtistCard
import com.example.learnjetpackcompose.Screen.Home.Component.TrackCard

@Composable
fun HomeScreen(
    onSettingClick: () -> Unit,
    onMyProfileClick: () -> Unit,
    onAlbumClick: () -> Unit = {},
    onTrackClick: () -> Unit = {},
    onArtistClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.processIntent(HomeIntent.LoadData)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderHome(
                onSettingClick = onSettingClick,
                onMyProfileClick = onMyProfileClick,
                avatar = state.userAvatar,
                displayName = state.displayName
            )
        }

        item { SectionTitle(text = "Rankings") }

        item { SectionHeader(title = "Top Albums", onSeeAll = {onAlbumClick(); viewModel.processIntent(HomeIntent.ShowAllAlbums) }) }
        item { AlbumsGrid(albums = state.topAlbums) }

        item { SectionHeader(title = "Top Tracks", onSeeAll = { onTrackClick(); viewModel.processIntent(HomeIntent.ShowAllTracks) }) }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(state.topTracks) { index, track ->
                    TrackCard(
                        name = track.name,
                        playcount = track.playcount,
                        artist = track.artist.name,
                        cover = track.image.firstOrNull()?.text ?: "",
                        bottomBarColor = state.trackCardColors[index % state.trackCardColors.size]
                    )
                }
            }
        }
        item { SectionHeader(title = "Top Artist", onSeeAll = { onArtistClick(); viewModel.processIntent(HomeIntent.ShowAllArtists) }) }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.topArtists) { artist ->
                    ArtistCard(name = artist.name, image = artist.image.firstOrNull { it.size == "extralarge"  }?.text ?: "")
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun HeaderHome(
    onSettingClick: () -> Unit = {},
    onMyProfileClick: () -> Unit,
    avatar: String?,
    displayName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onMyProfileClick() },
                placeholder = painterResource(R.drawable.default_avatar),
                error = painterResource(R.drawable.default_avatar)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = "Welcome back !", color = Color.White,
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = displayName, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
        IconButtonCustom(onClick = onSettingClick, icon = R.drawable.icon_settings,"Settings",
            modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(R.drawable.ranking),
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp),
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF39D3F5),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "See all",
            color = Color(0xFF00C2CB),
            fontSize = 12.sp,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}


@Composable
private fun AlbumsGrid(albums: List<Album>) {
    val limited = albums.take(6)
    val rows = (limited.size + 1) / 2
    val cardHeight = 60.dp
    val verticalSpacing = 12.dp
    val gridHeight = cardHeight * rows + verticalSpacing * (rows - 1) + 8.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(gridHeight),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
        contentPadding = PaddingValues(0.dp)
    ) {
        items(limited) { album ->
            AlbumCard(title = album.name, artist = album.artist.name, cover = album.image.firstOrNull { it.size == "extralarge" }?.text ?: "")
        }
    }
}



