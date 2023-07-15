package me.gurwi.fadedmusicbot.cache;

import lombok.Getter;
import me.gurwi.fadedmusicbot.enums.DisconnectReason;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {

    @Getter(lazy = true)
    private static final ConnectionManager instance = new ConnectionManager();

    public final Map<String, DisconnectReason> disconnectReasonMap = new HashMap<>();

    public void setLastDisconnectReason(Guild guild, DisconnectReason reason) {
        disconnectReasonMap.put(guild.getId(), reason);
    }

    public DisconnectReason getDisconnectReason(Guild guild) {
        if (!disconnectReasonMap.containsKey(guild.getId())) return DisconnectReason.DISCONNECTED;
        return disconnectReasonMap.get(guild.getId());
    }

}
