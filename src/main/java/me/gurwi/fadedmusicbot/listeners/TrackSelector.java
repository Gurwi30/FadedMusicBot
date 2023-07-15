package me.gurwi.fadedmusicbot.listeners;

import me.gurwi.fadedmusicbot.cache.ChannelsManager;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.BasicFunctions;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import me.gurwi.fadedmusicbot.utils.TrackUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TrackSelector extends ListenerAdapter {

    private final AudioPlayer audioPlayer = AudioPlayer.getInstance();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (!Objects.equals(event.getComponent().getId(), TrackUtils.getTrackSelectorId())) return;

        Guild guild = event.getGuild();
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        GuildVoiceState botVoiceState = event.getGuild().getSelfMember().getVoiceState();
        AudioChannelUnion botChannel = botVoiceState.getChannel();

        if (!BasicFunctions.inAudioChannel(event, memberVoiceState)) return;

        AudioManager audioManager = guild.getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        if (botVoiceState.inAudioChannel() && !botChannel.getId().equals(memberChannel.getId()) &&
                !botChannel.getMembers().stream().filter(m -> !m.getUser().isBot()).collect(Collectors.toList()).isEmpty() && audioPlayer.isPlaying(guild)) {
            event.replyEmbeds(EmbedUtils.getErrorEmbed("` ❌ Im already playing music in a another channel!`").build()).queue();
            return;
        }

        boolean delayAudioConnection = false;

        if (botVoiceState.inAudioChannel() && !botChannel.getId().equals(memberChannel.getId())) {
            audioPlayer.stopTrack(guild);
            audioManager.closeAudioConnection();
            delayAudioConnection = true;
        }

        ChannelsManager.getInstance().setLastMSGChannel(guild, event.getChannel());

        audioPlayer.loadAndPlay(event, true, delayAudioConnection, event.getChannel(), memberChannel, event.getGuild(),event.getValues().get(0));

        ////

        List<LayoutComponent> itemComponents = new ArrayList<>();

        event.getMessage().getComponents().forEach(msgButton -> {
            LayoutComponent editedButton = msgButton.asDisabled();
            itemComponents.add(editedButton);
        });

        event.editComponents(itemComponents).queue();
    }

}
