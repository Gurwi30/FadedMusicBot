package me.gurwi.fadedmusicbot.lavaplayer;

import lombok.Getter;

import java.util.Map;

public class AudioPlayerManager {

    @Getter
    private static final AudioPlayerManager instance = new AudioPlayerManager();

    private final Map<Long, GuildMusicManager> musicManagers;


}
