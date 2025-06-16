package com.teedee.firetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.teedee.firetv.ui.theme.FireTVHomeTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireTVHomeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FireTVHomeScreen()
                }
            }
        }
    }
}

@Composable
fun FireTVHomeScreen() {
    Row(modifier = Modifier.fillMaxSize()) {
        FireTVNavigationBar()

        Column(modifier = Modifier.fillMaxSize()) {
            FireTVCarousel()
            FireTVAppGrid()
        }
    }
}

@Composable
fun FireTVNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inputs", "Home", "Search", "Live TV", "Saved", "Add Apps", "Settings")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Home,
        Icons.Default.Home,
        Icons.Default.Home,
        Icons.Default.Home
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(
                color = Color(0xAA1C1C1E), // semi-transparent dark
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Gray, shape = MaterialTheme.shapes.extraLarge)
                .size(48.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clickable { selectedItem = index },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icons[index],
                    contentDescription = item,
                    tint = if (isSelected) Color.Black else Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    color = if (isSelected) Color.Black else Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun FireTVCarousel() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w1280/your_image.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Optional: glass-like overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0x99000000)) // semi-transparent overlay
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
        ) {
            Text("THE PERIPHERAL", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Text("New Episodes Fridays | Prime Video", color = Color.LightGray)
        }

        Text(
            text = "Wednesday, August 30  |  10:32 PM",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }
}

data class AppInfo(val name: String, val logoResId: Int)

@Composable
fun FireTVAppGrid() {
    val apps = listOf(
        AppInfo("Add Apps", R.drawable.add_apps),
        AppInfo("Netflix", R.drawable.netflix),
        AppInfo("Paramount+", R.drawable.paramount),
        AppInfo("Freevee", R.drawable.freevee),
        AppInfo("Hulu", R.drawable.hulu),
        AppInfo("Max", R.drawable.max),
        AppInfo("Disney+", R.drawable.disney),
        AppInfo("ESPN", R.drawable.espn),
        AppInfo("Philo", R.drawable.philo),
        AppInfo("FuboTV", R.drawable.fubotv),
        AppInfo("Prime Video", R.drawable.prime_video),
        AppInfo("YouTube TV", R.drawable.youtube_tv)
    )


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Reorder List",
                color = Color.White,
                modifier = Modifier
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(apps) { apps ->
                AppTile(appName = apps.name, logoResId = apps.logoResId)
            }
        }
    }
}

@Composable
fun AppTile(appName: String, logoResId: Int) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(90.dp)
            .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = appName,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

