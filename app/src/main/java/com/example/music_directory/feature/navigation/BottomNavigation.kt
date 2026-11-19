package com.example.music_directory.feature.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry


@Composable
fun BottomNavigation(modifier: Modifier = Modifier, onDestinationSelected: (Destination) -> Unit, currentDestination: NavBackStackEntry?) {

    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.background) {
        Destination.entries.forEach { destination ->

            NavigationBarItem(
                selected = currentDestination?.destination?.route == destination.route,
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
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    unselectedTextColor = MaterialTheme.colorScheme.outline,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}