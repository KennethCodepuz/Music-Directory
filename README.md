# Own-spotify

A personal Spotify-style music app for Android, backed by my own home server.

You search for a song, pick one of the results, and it plays. Every track someone streams is downloaded once and kept on the server, so the shared library grows from what people actually listen to.

> Name not decided yet. `Music-directory` in the code is a placeholder.

## How it works (planned)

```
Android app  ──►  Backend (home server)  ──►  YouTube API (search, top 10 results)
                        │
                        ├──►  yt-dlp (downloads streamed tracks)
                        └──►  Navidrome (stores and serves the music library)
```

- **Users:** me, plus a few friends and family who install the APK and sign up.
- **Access:** reachable from anywhere over the internet, with sign-ups gated.

## Status

| Part | State |
|---|---|
| Android app shell (Home / Search / Library tabs) | Done |
| Navigation (Navigation Compose) | Done |
| **v0: playlists CRUD** (app + Ktor server + Postgres) | Done. Deliberately naive; improving it in rounds |
| Home server exposure (TLS, remote access) | Not started |
| Search, playback, offline downloads | Not started |

## Tech stack

- **Android app:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, Ktor client
- **Backend:** Kotlin, Ktor, PostgreSQL (`server/`)
- **On-device storage (later):** SQLite, for offline downloads
- **Music storage:** Navidrome (self-hosted)
- **Downloading:** yt-dlp

## v0 API

| Method | Path | Does |
|---|---|---|
| GET | `/playlists` | List playlists with song counts |
| POST | `/playlists` | Create a playlist `{ "name" }` |
| GET | `/playlists/{id}` | One playlist with its songs |
| PUT | `/playlists/{id}` | Rename `{ "name" }` |
| DELETE | `/playlists/{id}` | Delete a playlist and its songs |
| POST | `/playlists/{id}/songs` | Add a song `{ "title", "artist" }` |
| DELETE | `/songs/{id}` | Remove a song |

## Running it locally

**1. Database (once).** Postgres must be running. Create the app's role and database, choosing your own password:

```bash
psql -U postgres -v app_password='pick-a-password' -f server/db/setup.sql
```

**2. Server.** From `server/`, with the same password (the tables are created on startup):

```powershell
$env:DB_PASSWORD = "pick-a-password"; .\gradlew.bat run
```

It listens on `127.0.0.1:8080` only, so nothing on your network can reach it.

**3. Phone.** With the phone connected over adb, forward its port 8080 to the PC, then install:

```bash
adb reverse tcp:8080 tcp:8080
./gradlew installDebug
```

Re-run `adb reverse` whenever the phone reconnects.

Other commands, from the repo root:

```bash
./gradlew testDebugUnitTest   # unit tests
./gradlew clean               # clear stale build output
```

## Docs

- `PRODUCT.md`: who the app is for, what it does, constraints and open decisions
- `CLAUDE.md`: instructions for the AI mentor in this repo
- `docs/`: design notes and decisions
