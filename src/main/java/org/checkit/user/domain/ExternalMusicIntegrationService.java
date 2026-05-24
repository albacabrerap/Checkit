package org.checkit.user.domain;

import org.springframework.stereotype.Service;

@Service
public class ExternalMusicIntegrationService {

    // Default Focus/Lofi Playlists
    private static final String DEFAULT_SPOTIFY = "https://open.spotify.com/playlist/37i9dQZF1DWWQRwui0ExPn";
    private static final String DEFAULT_YOUTUBE = "https://www.youtube.com/watch?v=Wo2G9740xyE&list=RDWo2G9740xyE&start_radio=1";
    private static final String DEFAULT_SOUNDCLOUD = "https://on.soundcloud.com/1DlkmihtL7oW5obDLM";
    private static final String DEFAULT_APPLEMUSIC= "https://music.apple.com/us/playlist/lo-fi-chill/pl.1d5ead185d8a4a9089f3b952770b762c";

    public String generateEmbeddedPlaybackLink(User user) {
        if (user.getCustomPlaylistUrl() != null && !user.getCustomPlaylistUrl().trim().isEmpty()) {
            return user.getCustomPlaylistUrl();
        }
        String platform = user.getMusicPlatform() != null ? user.getMusicPlatform().toUpperCase() : "SPOTIFY";

        return switch (platform) {
            case "YOUTUBE" -> DEFAULT_YOUTUBE;
            case "SOUNDCLOUD" -> DEFAULT_SOUNDCLOUD;
            case "APPLE MUSIC" -> DEFAULT_APPLEMUSIC;
            default -> DEFAULT_SPOTIFY;
        };
    }
}

