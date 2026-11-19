package musicdirectory

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun main() {
    val db = Database(
        url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/music_directory",
        user = System.getenv("DB_USER") ?: "music_directory",
        password = System.getenv("DB_PASSWORD") ?: error("Set the DB_PASSWORD environment variable"),
    )
    db.createTables()

    // Bound to localhost only: the phone reaches it through `adb reverse`, not over the network.
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) { json() }

        routing {
            get("/playlists") {
                call.respond(db.listPlaylists())
            }
            post("/playlists") {
                val input = call.receive<PlaylistInput>()
                call.respond(HttpStatusCode.Created, db.createPlaylist(input.name))
            }
            get("/playlists/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val playlist = db.getPlaylist(id)
                if (playlist == null) call.respond(HttpStatusCode.NotFound) else call.respond(playlist)
            }
            put("/playlists/{id}") {
                val id = call.parameters["id"]!!.toInt()
                db.renamePlaylist(id, call.receive<PlaylistInput>().name)
                call.respond(HttpStatusCode.NoContent)
            }
            delete("/playlists/{id}") {
                db.deletePlaylist(call.parameters["id"]!!.toInt())
                call.respond(HttpStatusCode.NoContent)
            }
            post("/playlists/{id}/songs") {
                val id = call.parameters["id"]!!.toInt()
                call.respond(HttpStatusCode.Created, db.addSong(id, call.receive<SongInput>()))
            }
            delete("/songs/{id}") {
                db.deleteSong(call.parameters["id"]!!.toInt())
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }.start(wait = true)
}
