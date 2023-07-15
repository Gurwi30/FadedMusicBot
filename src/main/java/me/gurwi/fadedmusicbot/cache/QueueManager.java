package me.gurwi.fadedmusicbot.cache;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import me.gurwi.fadedmusicbot.FadedMusicBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {

    @Getter(lazy = true)
    private static final QueueManager instance = new QueueManager();

    private final JDA jda = FadedMusicBot.getJda();

    @Getter
    private final Map<String, String> queueTrack = new HashMap<>();

    public void addTrack(User user, AudioTrack audioTrack) {
        queueTrack.put(audioTrack.getIdentifier(), user.getId());
    }

    public User getWhoQueued(AudioTrack track) {
        return jda.getUserById(queueTrack.get(track.getIdentifier()));
    }

}
