package com.example.music_directory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.music_directory.feature.home.HomeScreen
import com.example.music_directory.feature.library.LibraryScreen
import com.example.music_directory.feature.navigation.BottomNavigation
import com.example.music_directory.feature.navigation.Destination
import com.example.music_directory.feature.playlist.PlaylistScreen
import com.example.music_directory.feature.search.SearchScreen
import com.example.music_directory.ui.theme.MusicdirectoryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicdirectoryTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { BottomNavigation(
        currentDestination = backStackEntry,
        onDestinationSelected = {destination -> navController.navigate(destination.route) {
            popUpTo(Destination.HOME.route)
            launchSingleTop = true
        }}
    ) }) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(modifier = Modifier.padding(innerPadding))
            }
            composable("search") {
                SearchScreen(modifier = Modifier.padding(innerPadding))
            }
            composable("library") {
                LibraryScreen(
                    onPlaylistClick = { id -> navController.navigate("playlist/$id") },
                    modifier = Modifier.padding(innerPadding),
                )
            }
            composable("playlist/{id}") { entry ->
                PlaylistScreen(
                    playlistId = entry.arguments!!.getString("id")!!.toInt(),
                    onBack = { navController.popBackStack() },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}


@Preview
@Composable
fun MainActivityPreview() {
    MusicdirectoryTheme {
        MainScreen()
    }
}