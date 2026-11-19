package com.example.music_directory.feature.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Box
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
import com.example.music_directory.data.PlaylistDetail
import com.example.music_directory.data.Song
import com.example.music_directory.ui.components.PlaylistArtwork
import kotlinx.coroutines.launch

@Composable
fun PlaylistScreen(playlistId: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    var playlist by remember { mutableStateOf<PlaylistDetail?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    suspend fun reload() {
        playlist = ApiClient.getPlaylist(playlistId)
    }

    LaunchedEffect(playlistId) { reload() }

    Column(modifier = modifier) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        val current = playlist ?: return@Column

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PlaylistArtwork(playlistId = current.id, size = 220.dp)
                }
                Text(
                    current.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Playlist • ${current.songs.size} songs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("Add song", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            items(current.songs) { song ->
                SongRow(
                    song = song,
                    onRemove = {
                        scope.launch {
                            ApiClient.deleteSong(song.id)
                            reload()
                        }
                    },
                )
            }
        }
    }

    if (showAddDialog) {
        AddSongDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, artist ->
                showAddDialog = false
                scope.launch {
                    ApiClient.addSong(playlistId, title, artist)
                    reload()
                }
            },
        )
    }
}

@Composable
private fun SongRow(song: Song, onRemove: () -> Unit) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(song.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                song.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options for ${song.title}")
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(text = { Text("Remove from playlist") }, onClick = { menuOpen = false; onRemove() })
            }
        }
    }
}

@Composable
private fun AddSongDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add song") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, singleLine = true)
                OutlinedTextField(value = artist, onValueChange = { artist = it }, label = { Text("Artist") }, singleLine = true)
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(title, artist) }) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
