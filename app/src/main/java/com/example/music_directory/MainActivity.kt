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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.music_directory.feature.home.HomeScreen
import com.example.music_directory.feature.library.LibraryScreen
import com.example.music_directory.feature.navigation.BottomNavigation
import com.example.music_directory.feature.navigation.Destination

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
    var currentDestination by rememberSaveable {mutableStateOf(Destination.HOME)}
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { BottomNavigation(
        currentDestination = currentDestination,
        onDestinationSelected = {destination -> currentDestination = destination}
    ) }) { innerPadding ->

        when (currentDestination) {
            Destination.HOME ->
                HomeScreen(modifier = Modifier.padding(innerPadding))

            Destination.SEARCH ->
                SearchScreen(modifier = Modifier.padding(innerPadding))

            Destination.LIBRARY ->
                LibraryScreen(modifier = Modifier.padding(innerPadding))
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