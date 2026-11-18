package com.example.music_directory.feature.home


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.music_directory.ui.theme.MusicdirectoryTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Home Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MusicdirectoryTheme {
        HomeScreen()
    }
}
