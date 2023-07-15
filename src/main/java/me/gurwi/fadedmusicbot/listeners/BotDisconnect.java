package me.gurwi.fadedmusicbot.listeners;

import me.gurwi.fadedmusicbot.cache.ChannelsManager;
import me.gurwi.fadedmusicbot.cache.ConnectionManager;
import me.gurwi.fadedmusicbot.enums.DisconnectReason;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotDisconnect extends ListenerAdapter {

    private final AudioPlayer audioPlayer = AudioPlayer.getInstance();

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

        if (event.getChannelLeft() == null) return;

        Member bot = event.getGuild().getSelfMember();
        Member whoLeft = event.getEntity();
        Guild guild = event.getGuild();

        if (!bot.getId().equals(whoLeft.getId())) return;
        if (ConnectionManager.getInstance().getDisconnectReason(guild) != DisconnectReason.DISCONNECTED) return;

        audioPlayer.stopTrack(guild);
        audioPlayer.setPaused(guild, false);
        audioPlayer.loop(guild, false);
        audioPlayer.getMusicManager(guild).getScheduler().getQueue().clear();

        MessageChannelUnion channel = ChannelsManager.getInstance().getGuildChat(guild.getId());

        if (channel == null) return;

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(EmbedUtils.getDefaultColor());
        embedBuilder.setDescription("`🛑 The current track was stopped successfully!`");

        channel.sendMessageEmbeds(embedBuilder.build()).queue();

        ChannelsManager.getInstance().getLastChatMessageChannel().remove(guild.getId());

    }
}
