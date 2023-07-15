package me.gurwi.fadedmusicbot.cache;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class ChannelsManager {

    @Getter(lazy = true)
    private static final ChannelsManager instance = new ChannelsManager();

    @Getter
    private final Map<String, MessageChannelUnion> lastChatMessageChannel = new HashMap<>();

    public void setLastMSGChannel(Guild guild, MessageChannelUnion channel) {
        lastChatMessageChannel.put(guild.getId(), channel);
    }

    public MessageChannelUnion getGuildChat(String guildId) {
        return lastChatMessageChannel.get(guildId);
    }

}
