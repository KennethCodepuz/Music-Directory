package musicdirectory

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSummary(val id: Int, val name: String, val songCount: Int)

@Serializable
data class Song(val id: Int, val title: String, val artist: String)

@Serializable
data class PlaylistDetail(val id: Int, val name: String, val songs: List<Song>)

@Serializable
data class PlaylistInput(val name: String)

@Serializable
data class SongInput(val title: String, val artist: String)

class Database(private val url: String, private val user: String, private val password: String) {

    // v0: opens a new connection for every query.
    private fun <T> withConnection(block: (Connection) -> T): T =
        DriverManager.getConnection(url, user, password).use(block)

    fun createTables() = withConnection { conn ->
        conn.createStatement().use {
            it.execute(
                """
                CREATE TABLE IF NOT EXISTS playlists (
                    id   SERIAL PRIMARY KEY,
                    name TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS songs (
                    id          SERIAL PRIMARY KEY,
                    playlist_id INT NOT NULL REFERENCES playlists(id),
                    title       TEXT NOT NULL,
                    artist      TEXT NOT NULL
                );
                """
            )
        }
    }

    fun listPlaylists(): List<PlaylistSummary> = withConnection { conn ->
        conn.prepareStatement(
            "SELECT p.id, p.name, (SELECT COUNT(*) FROM songs s WHERE s.playlist_id = p.id) AS song_count " +
                "FROM playlists p ORDER BY p.id"
        ).use { stmt ->
            val rs = stmt.executeQuery()
            buildList {
                while (rs.next()) add(PlaylistSummary(rs.getInt("id"), rs.getString("name"), rs.getInt("song_count")))
            }
        }
    }

    fun getPlaylist(id: Int): PlaylistDetail? = withConnection { conn ->
        val name = conn.prepareStatement("SELECT name FROM playlists WHERE id = ?").use { stmt ->
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getString("name") else null
        } ?: return@withConnection null

        val songs = conn.prepareStatement("SELECT id, title, artist FROM songs WHERE playlist_id = ? ORDER BY id").use { stmt ->
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            buildList {
                while (rs.next()) add(Song(rs.getInt("id"), rs.getString("title"), rs.getString("artist")))
            }
        }
        PlaylistDetail(id, name, songs)
    }

    fun createPlaylist(name: String): PlaylistSummary = withConnection { conn ->
        conn.prepareStatement("INSERT INTO playlists (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setString(1, name)
            stmt.executeUpdate()
            val keys = stmt.generatedKeys
            keys.next()
            PlaylistSummary(keys.getInt(1), name, 0)
        }
    }

    fun renamePlaylist(id: Int, name: String) = withConnection { conn ->
        conn.prepareStatement("UPDATE playlists SET name = ? WHERE id = ?").use { stmt ->
            stmt.setString(1, name)
            stmt.setInt(2, id)
            stmt.executeUpdate()
        }
    }

    fun deletePlaylist(id: Int) = withConnection { conn ->
        conn.prepareStatement("DELETE FROM songs WHERE playlist_id = ?").use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
        conn.prepareStatement("DELETE FROM playlists WHERE id = ?").use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

    fun addSong(playlistId: Int, input: SongInput): Song = withConnection { conn ->
        conn.prepareStatement(
            "INSERT INTO songs (playlist_id, title, artist) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        ).use { stmt ->
            stmt.setInt(1, playlistId)
            stmt.setString(2, input.title)
            stmt.setString(3, input.artist)
            stmt.executeUpdate()
            val keys = stmt.generatedKeys
            keys.next()
            Song(keys.getInt(1), input.title, input.artist)
        }
    }

    fun deleteSong(id: Int) = withConnection { conn ->
        conn.prepareStatement("DELETE FROM songs WHERE id = ?").use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }
}
