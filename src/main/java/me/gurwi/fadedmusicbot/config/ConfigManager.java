package me.gurwi.fadedmusicbot.config;

import lombok.RequiredArgsConstructor;
import me.gurwi.fadedmusicbot.FadedMusicBot;
import net.bobolabs.config.Configuration;

@RequiredArgsConstructor
public enum ConfigManager {

    TOKEN("Bot-Token"),
    BOT_OWNER_ID("Bot-Owner-Id"),

    BOT_STATUS("Bot-Status"),
    BOT_ACTIVITY("Bot-Activity.activity"),
    BOT_ACTIVITY_NAME("Bot-Activity.name"),
    BOT_ACTIVITY_URL("Bot-Activity.url");

    ///

    private final YamlConfig botConfig = FadedMusicBot.getBotConfig();
    private final Configuration config = botConfig.getConfig();
    private final String path;

    // METHODS

    public String getString() {
        return config.getString(path);
    }

    public boolean isEmpty() {
        return config.getString(path).isEmpty();
    }

}
