package me.gurwi.fadedmusicbot.commands;

import me.gurwi.fadedmusicbot.FadedMusicBot;
import me.gurwi.fadedmusicbot.cache.ConnectionManager;
import me.gurwi.fadedmusicbot.commands.base.BotCommand;
import me.gurwi.fadedmusicbot.enums.DisconnectReason;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StopCommand extends BotCommand {

    public StopCommand() {
        super("stop", "Stops the playlist");
    }

    private static final AudioPlayer audioPlayer = AudioPlayer.getInstance();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        stopTrack(event, false, event.getChannel());
    }

    @SuppressWarnings("ConstantConditions")
    public static void stopTrack(IReplyCallback event, boolean chatReply, MessageChannelUnion channel) {

        Member member = event.getMember();
        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        GuildVoiceState botVoiceState = event.getGuild().getSelfMember().getVoiceState();

        if (!audioPlayer.isPlaying(guild) || !Objects.requireNonNull(botVoiceState).inAudioChannel()) {

            EmbedBuilder errorEmbed = EmbedUtils.getErrorEmbed("` ❌ The bot isn't playing any track`");

            if (chatReply) {
                channel.sendMessageEmbeds(errorEmbed.build()).queue();
            } else {
                event.replyEmbeds(errorEmbed.build()).queue();
            }

            return;
        }

        audioPlayer.stopTrack(guild);
        if (audioPlayer.isTrackPause(guild)) audioPlayer.setPaused(guild, false);
        if (audioPlayer.isLoop(guild)) audioPlayer.loop(guild, false);
        audioPlayer.getMusicManager(guild).getScheduler().getQueue().clear();

        audioManager.closeAudioConnection();

        ConnectionManager.getInstance().setLastDisconnectReason(guild, DisconnectReason.STOP_COMMAND);
        FadedMusicBot.getScheduler().schedule(() -> ConnectionManager.getInstance().setLastDisconnectReason(guild, DisconnectReason.DISCONNECTED), 1, TimeUnit.SECONDS);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(EmbedUtils.getDefaultColor());
        embedBuilder.setAuthor("Sent by " + member.getEffectiveName(), member.getAvatarUrl(), member.getUser().getAvatarUrl());
        embedBuilder.setDescription("`🛑 The current track was stopped successfully!`");

        if (chatReply) {
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.build()).queue();
        }

    }

}
