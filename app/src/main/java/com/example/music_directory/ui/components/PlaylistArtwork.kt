package com.example.music_directory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

// Gradient pairs picked per playlist id, so each playlist keeps the same cover.
private val coverGradients = listOf(
    Color(0xFF4A3AFF) to Color(0xFF8FD3C9),
    Color(0xFF6B3FA0) to Color(0xFF2E1A47),
    Color(0xFFB23A2E) to Color(0xFF4A1410),
    Color(0xFF1E7A4C) to Color(0xFF0E2E20),
    Color(0xFF2A5DA8) to Color(0xFF101C33),
)

@Composable
fun PlaylistArtwork(playlistId: Int, size: Dp, modifier: Modifier = Modifier) {
    val (start, end) = coverGradients[playlistId.mod(coverGradients.size)]
    Box(
        modifier = modifier
            .size(size)
            .background(Brush.linearGradient(listOf(start, end))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.MusicNote,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.4f),
        )
    }
}
