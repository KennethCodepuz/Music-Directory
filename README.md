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
| Android app shell (Home / Search / Library tabs) | In progress |
| Navigation (Navigation Compose) | Next |
| Backend | Not started (design first) |
| Home server exposure (TLS, remote access) | Not started |
| Search, playback, library, offline downloads | Not started |

## Tech stack

- **Android app:** Kotlin, Jetpack Compose, Material 3, Navigation Compose
- **Backend:** undecided
- **Music storage:** Navidrome (self-hosted)
- **Downloading:** yt-dlp

## Running the Android app

Requires Android Studio, or a JDK with the Android SDK. Run from the repo root:

```bash
./gradlew installDebug        # build and install on a connected phone or emulator
./gradlew testDebugUnitTest   # unit tests
./gradlew clean               # clear stale build output
```

## Docs

- `PRODUCT.md`: who the app is for, what it does, constraints and open decisions
- `CLAUDE.md`: instructions for the AI mentor in this repo
- `docs/`: design notes and decisions
