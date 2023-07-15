package me.gurwi.fadedmusicbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class BasicFunctions {

    public static boolean isYoutubeUrl(String youTubeURl) {
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        return (!youTubeURl.isEmpty() && youTubeURl.matches(pattern));
    }

    public static boolean inAudioChannel(IReplyCallback event, GuildVoiceState voiceState) {

        EmbedBuilder errorEmbed = EmbedUtils.getErrorEmbed("` ❌ You have to be in a voice channel to play a song`");

        if (!voiceState.inAudioChannel()) {
            event.replyEmbeds(errorEmbed.build()).setEphemeral(true).queue();
            return false;
        }

        return true;
    }

}
