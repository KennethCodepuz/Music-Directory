# Product

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

- **Primary:** the owner, who builds and runs the app and home server, and listens on their own Android phone at home and while travelling.
- **Secondary:** a few friends or family members. They install a sideloaded APK the owner gives them and create their own accounts in the app.

All users listen on mobile, often away from home and sometimes with poor or no connectivity.

## Product Purpose

A personal Spotify-style music app backed by the owner's own home server and reachable from anywhere. A user searches for a song, picks one of the results, and plays it. The library grows from what people actually listen to.

Success means every user can find a song, play it straight away, and keep listening in the background or offline, all without a commercial streaming subscription.

## Positioning

The library has no catalog of its own. It builds itself: whatever any user streams gets downloaded and kept on the owner's server for everyone to use later. The owner holds the music files.

## Operating Context

- **Search:** the backend queries the YouTube API and the app shows the top 10 results.
- **Acquisition:** when a user streams a track, the backend automatically downloads it with yt-dlp and stores it in Navidrome on the home server. Navidrome is a self-hosted music server with a Subsonic-compatible API.
- **Playback:** later plays, the library, playlists and offline downloads come from the stored copy.
- **Distribution:** the app ships as an APK the owner hands out directly. It is not on the Play Store.
- **Network:** the home server is built and run by the owner and has to be reachable over the internet.

## Capabilities and Constraints

Must-haves:
- Search, with the top 10 YouTube results shown in the app
- Background playback, with lock-screen and notification controls
- Offline downloads to the device
- Playlists and a library (likes, plus browsing by artist and album)
- Multiple user accounts with self sign-up from the app

Constraints and open decisions:
- **Client:** native Android in Kotlin and Jetpack Compose (existing code in `app/`).
- **Backend:** not built yet. Its language, framework, API design, and how it talks to Navidrome are all undecided.
- **Sign-up gating:** open. Self sign-up on an internet-facing server needs a gate, such as an invite code or approval by the owner. The mechanism is undecided.
- **Remote access:** how the home server is exposed is undecided. Options include a reverse proxy with TLS, a VPN, or a tunnel.
- **Per-user vs shared data:** the downloaded library is shared. Which data stays per-user (likes, playlists, history) is undecided.
- **Content-source risk:** downloading from YouTube with yt-dlp goes against YouTube's Terms of Service. Distributing copyrighted audio to other people carries legal risk. The design should keep the source behind a swappable boundary.
- **Product name:** undecided. `Music-directory` / `Musicdirectory` in code is a placeholder.

## Evidence on Hand

There is no real content, branding, logo or user data yet. Only Android scaffolding exists: a home screen and a bottom navigation bar for Home, Search and Library. Don't make up a name, logo, testimonials or usage numbers.

## Product Principles

1. **Play first, catalog later.** Getting from a search to sound playing should feel instant, even while the server is still downloading the track in the background.
2. **Works with bad signal.** Being offline or on a flaky connection is normal, not an edge case. Downloaded music and the user's own data must stay usable.
3. **Private by default.** The server is on the open internet but serves only a few known people. Access is gated and nothing is public.
4. **Shared library, personal space.** Everyone benefits from the growing library, while each person's listening stays their own.
