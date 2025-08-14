package com.example.learnjetpackcompose.Screen.Home.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.Screen.Home.HomeViewModel

@Composable
fun TopArtistScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()
        .background(Color.Black)
        .padding(12.dp)){

        HeaderTopAlbums(onBack, stringResource(R.string.top_artist))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(count = state.topArtists.size) { index ->
                val artist = state.topArtists[index]
                ArtistCard(
                    name = artist.name,
                    image = artist.image.firstOrNull()?.text
                )
            }
        }
    }
}


@Preview
@Composable
fun ArtistCard(name: String ="Taylor Swift", image: String? = "") {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(180.dp, 160.dp)
    ) {
        Box(){
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().size(200.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.rose),
                error = painterResource(R.drawable.rose)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF789CB7))
                        )
                    )
            )
            Box(
                modifier = Modifier.padding(16.dp)
            ){
                Text(text = name,
                    color = Color.White,
                    fontSize = 16.sp)
            }
        }
    }
}

