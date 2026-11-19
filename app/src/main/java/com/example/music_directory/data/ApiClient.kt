package com.example.music_directory.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSummary(val id: Int, val name: String, val songCount: Int)

@Serializable
data class Song(val id: Int, val title: String, val artist: String)

@Serializable
data class PlaylistDetail(val id: Int, val name: String, val songs: List<Song>)

@Serializable
private data class PlaylistInput(val name: String)

@Serializable
private data class SongInput(val title: String, val artist: String)

object ApiClient {
    // The phone reaches the server on your PC through `adb reverse tcp:8080 tcp:8080`.
    private const val BASE_URL = "http://localhost:8080"

    private val http = HttpClient(OkHttp) {
        install(ContentNegotiation) { json() }
    }

    suspend fun getPlaylists(): List<PlaylistSummary> =
        http.get("$BASE_URL/playlists").body()

    suspend fun getPlaylist(id: Int): PlaylistDetail =
        http.get("$BASE_URL/playlists/$id").body()

    suspend fun createPlaylist(name: String) {
        http.post("$BASE_URL/playlists") {
            contentType(ContentType.Application.Json)
            setBody(PlaylistInput(name))
        }
    }

    suspend fun renamePlaylist(id: Int, name: String) {
        http.put("$BASE_URL/playlists/$id") {
            contentType(ContentType.Application.Json)
            setBody(PlaylistInput(name))
        }
    }

    suspend fun deletePlaylist(id: Int) {
        http.delete("$BASE_URL/playlists/$id")
    }

    suspend fun addSong(playlistId: Int, title: String, artist: String) {
        http.post("$BASE_URL/playlists/$playlistId/songs") {
            contentType(ContentType.Application.Json)
            setBody(SongInput(title, artist))
        }
    }

    suspend fun deleteSong(id: Int) {
        http.delete("$BASE_URL/songs/$id")
    }
}
