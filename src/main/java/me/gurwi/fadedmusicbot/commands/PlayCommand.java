package me.gurwi.fadedmusicbot.commands;

import me.gurwi.fadedmusicbot.cache.ChannelsManager;
import me.gurwi.fadedmusicbot.commands.base.BotCommand;
import me.gurwi.fadedmusicbot.commands.base.CMDOption;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.BasicFunctions;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.stream.Collectors;

public class PlayCommand extends BotCommand {

    public PlayCommand() {
        super("play", "Plays a song", new CMDOption(OptionType.STRING, "Song", "Song name or link", true));
    }

    private final AudioPlayer audioPlayer = AudioPlayer.getInstance();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Guild guild = event.getGuild();
        Member member = event.getMember();
        Member bot = guild.getSelfMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();

        if (!BasicFunctions.inAudioChannel(event, memberVoiceState)) return;

        AudioManager audioManager = guild.getAudioManager();

        VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();
        AudioChannelUnion botChannel = botVoiceState.getChannel();

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

        audioPlayer.loadAndPlay(event, false, delayAudioConnection, event.getChannel(), memberChannel, guild, event.getOption("song").getAsString());
    }

}
