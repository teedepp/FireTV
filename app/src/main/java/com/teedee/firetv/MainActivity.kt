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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teedee.firetv.ui.theme.FireTVTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireTVTheme {
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.blurred_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = 0.9f
                }
        )

        Row(modifier = Modifier.fillMaxSize()) {
            FireTVNavigationBar()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 32.dp)
            ) {
                CurrentDateTime()
                FireTVAutoCarousel(
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )
                FireTVAppGrid()
            }
        }
    }
}

@Composable
fun FireTVNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("Inputs", "Home", "Search", "Live TV", "Saved", "Add Apps", "Settings", "FireCircle")
    val icons = listOf(
        R.drawable.ic_inputs,
        R.drawable.ic_home,
        R.drawable.ic_search,
        R.drawable.ic_livetv,
        R.drawable.ic_saved,
        R.drawable.ic_addapps,
        R.drawable.ic_settings,
        R.drawable.ic_firecircle
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .width(150.dp)
    ) {
        // Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF292929)),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Trideb Dhar",
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1B1B1B))
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEachIndexed { index, label ->
                val isSelected = selectedItem == index

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { selectedItem = index }
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label,
                        tint = if (isSelected) Color.Black else Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = label,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                    if (label == "FireCircle") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_new),
                            contentDescription = "New",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp) // Adjusted size for consistency
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CurrentDateTime() {
    var currentTime by remember { mutableStateOf(getFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = getFormattedTime()
        }
    }

    Text(
        text = currentTime,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

fun getFormattedTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d · h:mm a")
    return current.format(formatter)
}

@Composable
fun FireTVAutoCarousel(modifier: Modifier = Modifier) {
    val items = listOf(
        R.drawable.the_peripheral,
        R.drawable.game_of_thrones,
        R.drawable.breaking_bad
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { items.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        ) { page ->
            Image(
                painter = painterResource(id = items[page]),
                contentDescription = "Carousel Item",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            repeat(items.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) Color.White else Color.Gray)
                )
            }
        }
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
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(apps) { app ->
                AppTile(appName = app.name, logoResId = app.logoResId)
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
