package com.example.music_directory.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

@Composable
fun BottomNavigation(modifier: Modifier = Modifier, currentDestination: Destination, onDestinationSelected: (Destination) -> Unit) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.background) {
        Destination.entries.forEach { destination ->

            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = {
                    onDestinationSelected(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = {
                    Text(destination.label)
                }
            )
        }
    }
}