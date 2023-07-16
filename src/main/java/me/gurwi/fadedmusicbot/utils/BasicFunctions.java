package me.gurwi.fadedmusicbot.utils;

import me.gurwi.fadedmusicbot.enums.URLSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class BasicFunctions {

    @SuppressWarnings("all")
    public static URLSource getUrlSource(String url) {
        String youtubeRegex = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        String soundCloudRegex = "^(?:https?:\\/\\/)((?:www\\.)|(?:m\\.))?soundcloud\\.com\\/[a-z0-9](?!.*?(-|_){2})[\\w-]{1,23}[a-z0-9](?:\\/.+)?$";
        String spotifyRegex = "^(?:https?:\\/\\/(?:open\\.spotify\\.com\\/(track|playlist)\\/|spotify:\\/{1,2}(track|playlist):))(?:[a-zA-Z0-9]+)(?:\\?.*)?$";
        String twitchRegex = "^(?:https?:\\/\\/)?(?:www\\.|go\\.)?twitch\\.tv\\/([a-z0-9_]+)($|\\?)";
        String genericURL = "^(https?:\\/\\/)?([^\\s\\/]+\\.)*[^\\s\\/]+\\.[^\\s\\/]+(\\/[^\\s\\/]+)*\\/?$";

        if (url.isEmpty()) return URLSource.NOT_URL;
        if (url.matches(youtubeRegex)) return URLSource.YOUTUBE;
        if (url.matches(soundCloudRegex)) return URLSource.SOUNDCLOUD;
        if (url.matches(spotifyRegex)) return URLSource.SPOTIFY;
        if (url.matches(twitchRegex)) return URLSource.TWITCH;
        if (url.matches(genericURL)) return URLSource.GENERIC_URL;

        return URLSource.NOT_URL;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean inAudioChannel(IReplyCallback event, GuildVoiceState voiceState) {

        EmbedBuilder errorEmbed = EmbedUtils.getErrorEmbed("` ❌ You have to be in a voice channel to play a song`");

        if (!voiceState.inAudioChannel()) {
            event.replyEmbeds(errorEmbed.build()).setEphemeral(true).queue();
            return false;
        }

        return true;
    }

    public static  <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
        if (enumName == null) {
            return false;
        } else {
            try {
                Enum.valueOf(enumClass, enumName);
                return true;
            } catch (IllegalArgumentException var3) {
                return false;
            }
        }
    }

    public static String startUpText() {

        return ConsoleColors.PURPLE_BRIGHT +
                " ______   ______     _____     ______     _____     __    __     __  __     ______     __     ______     ______     ______     ______  \n" +
                "/\\  ___\\ /\\  __ \\   /\\  __-.  /\\  ___\\   /\\  __-.  /\\ \"-./  \\   /\\ \\/\\ \\   /\\  ___\\   /\\ \\   /\\  ___\\   /\\  == \\   /\\  __ \\   /\\__  _\\ \n" +
                "\\ \\  __\\ \\ \\  __ \\  \\ \\ \\/\\ \\ \\ \\  __\\   \\ \\ \\/\\ \\ \\ \\ \\-./\\ \\  \\ \\ \\_\\ \\  \\ \\___  \\  \\ \\ \\  \\ \\ \\____  \\ \\  __<   \\ \\ \\/\\ \\  \\/_/\\ \\/ \n" +
                " \\ \\_\\    \\ \\_\\ \\_\\  \\ \\____-  \\ \\_____\\  \\ \\____-  \\ \\_\\ \\ \\_\\  \\ \\_____\\  \\/\\_____\\  \\ \\_\\  \\ \\_____\\  \\ \\_____\\  \\ \\_____\\    \\ \\_\\ \n" +
                "  \\/_/     \\/_/\\/_/   \\/____/   \\/_____/   \\/____/   \\/_/  \\/_/   \\/_____/   \\/_____/   \\/_/   \\/_____/   \\/_____/   \\/_____/     \\/_/ \n" +
                "   " + ConsoleColors.PURPLE_UNDERLINED + "By @Gurwi30" + ConsoleColors.RESET + "\n";
    }

}
