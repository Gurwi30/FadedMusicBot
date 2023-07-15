package me.gurwi.fadedmusicbot.utils;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class EmbedUtils {

    @Getter
    private static final Color defaultColor = new Color(0xF073FF);
    @Getter
    private static final String TITLE = "› \uD835\uDDD9\uD835\uDDEE\uD835\uDDF1\uD835\uDDF2\uD835\uDDF1\uD835\uDDE0\uD835\uDE02\uD835\uDE00\uD835\uDDF6\uD835\uDDF0";
    @Getter
    private static final String FOOTER = "💎 @FadedStudios";

    public static EmbedBuilder getErrorEmbed(String error) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setFooter(FOOTER);
        embedBuilder.setColor(new Color(0xa80000));
        embedBuilder.setTitle(TITLE);
        embedBuilder.setDescription(error);

        return embedBuilder;
    }

}
