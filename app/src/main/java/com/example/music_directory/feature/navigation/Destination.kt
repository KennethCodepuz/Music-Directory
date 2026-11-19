package com.example.music_directory.feature.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Filled.Home),
    SEARCH("search", "Search", Icons.Filled.Search),
    LIBRARY("library", "Your Library", Icons.Filled.LibraryMusic)
}
