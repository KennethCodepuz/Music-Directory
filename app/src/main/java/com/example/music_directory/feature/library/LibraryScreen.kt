package com.example.music_directory.feature.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.music_directory.data.ApiClient
import com.example.music_directory.data.PlaylistSummary
import com.example.music_directory.ui.components.PlaylistArtwork
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(onPlaylistClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    var playlists by remember { mutableStateOf(listOf<PlaylistSummary>()) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var renaming by remember { mutableStateOf<PlaylistSummary?>(null) }
    val scope = rememberCoroutineScope()

    suspend fun reload() {
        playlists = ApiClient.getPlaylists()
    }

    LaunchedEffect(Unit) { reload() }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Your Library",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Create playlist")
            }
        }

        LazyColumn {
            items(playlists) { playlist ->
                PlaylistRow(
                    playlist = playlist,
                    onClick = { onPlaylistClick(playlist.id) },
                    onRename = { renaming = playlist },
                    onDelete = {
                        scope.launch {
                            ApiClient.deletePlaylist(playlist.id)
                            reload()
                        }
                    },
                )
            }
        }
    }

    if (showCreateDialog) {
        NameDialog(
            title = "Create playlist",
            initialName = "",
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                showCreateDialog = false
                scope.launch {
                    ApiClient.createPlaylist(name)
                    reload()
                }
            },
        )
    }

    renaming?.let { playlist ->
        NameDialog(
            title = "Rename playlist",
            initialName = playlist.name,
            onDismiss = { renaming = null },
            onConfirm = { name ->
                renaming = null
                scope.launch {
                    ApiClient.renamePlaylist(playlist.id, name)
                    reload()
                }
            },
        )
    }
}

@Composable
private fun PlaylistRow(
    playlist: PlaylistSummary,
    onClick: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlaylistArtwork(playlistId = playlist.id, size = 64.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                playlist.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "Playlist • ${playlist.songCount} songs",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options for ${playlist.name}")
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(text = { Text("Rename") }, onClick = { menuOpen = false; onRename() })
                DropdownMenuItem(text = { Text("Delete") }, onClick = { menuOpen = false; onDelete() })
            }
        }
    }
}

@Composable
private fun NameDialog(
    title: String,
    initialName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
        },
        confirmButton = { TextButton(onClick = { onConfirm(name) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
