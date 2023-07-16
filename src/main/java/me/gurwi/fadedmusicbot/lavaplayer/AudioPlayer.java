package me.gurwi.fadedmusicbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import me.gurwi.fadedmusicbot.FadedMusicBot;
import me.gurwi.fadedmusicbot.cache.QueueManager;
import me.gurwi.fadedmusicbot.enums.URLSource;
import me.gurwi.fadedmusicbot.utils.BasicFunctions;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import me.gurwi.fadedmusicbot.utils.TrackUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class AudioPlayer {

    @Getter(lazy = true)
    private static final AudioPlayer instance = new AudioPlayer();

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public AudioPlayer() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager, guild.getId());
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    @SuppressWarnings("ConstantConditions")
    public void loadAndPlay(IReplyCallback event, boolean chatReply, boolean delayAudioConnection, MessageChannelUnion channel, VoiceChannel voiceChannel, Guild guild, String trackUrl) {
        GuildMusicManager musicManager = getMusicManager(guild);
        String trackToLoad;

        URLSource urlSource = BasicFunctions.getUrlSource(trackUrl);

        if (urlSource != URLSource.NOT_URL) {
            trackToLoad = trackUrl;
        } else {
            trackToLoad = "ytsearch:" + trackUrl;
        }

        Member member = event.getMember();

        EmbedBuilder embedBuilder = new EmbedBuilder();

        audioPlayerManager.loadItemOrdered(musicManager, trackToLoad, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                AudioManager audioManager = guild.getAudioManager();

                if (delayAudioConnection) {
                    FadedMusicBot.getScheduler().schedule(() -> audioManager.openAudioConnection(voiceChannel), 1, TimeUnit.SECONDS);
                } else {
                    audioManager.openAudioConnection(voiceChannel);
                }

                boolean addedToQueue = false;

                if (isPlaying(guild)) {
                    embedBuilder.setDescription("`➕ " + track.getInfo().title + " added to the queue`");
                    addedToQueue = true;
                } else {
                    embedBuilder.setAuthor("Sent by " + member.getEffectiveName(), member.getAvatarUrl(), member.getUser().getAvatarUrl());
                    embedBuilder.setColor(EmbedUtils.getDefaultColor());
                    embedBuilder.setTitle(EmbedUtils.getTITLE());

                    Duration duration = Duration.ofMillis(track.getDuration());
                    long seconds = duration.getSeconds();

                    long HH = seconds / 3600;
                    long MM = (seconds % 3600) / 60;
                    long SS = seconds % 60;

                    String timeInHHMMSS = String.format("%02d:%02d:%02d", HH, MM, SS);
                    if (track.getInfo().isStream) timeInHHMMSS = "Live";

                    MessageEmbed.Field trackNameField = new MessageEmbed.Field("**🎵 Now playing **", "`" + track.getInfo().title + "`", false);
                    MessageEmbed.Field trackDurationField = new MessageEmbed.Field("**ℹ️ Other Info**", "``` Duration: " + timeInHHMMSS + "\n" +
                            " Author: " + track.getInfo().author + " ```", false);

                    embedBuilder.addField(trackNameField);
                    embedBuilder.addField(trackDurationField);

                    embedBuilder.setThumbnail("https://img.youtube.com/vi/" + track.getInfo().identifier + "/hqdefault.jpg");

                }

                musicManager.getScheduler().queue(track);
                QueueManager.getInstance().addTrack(member.getUser(), track);

                if (chatReply && addedToQueue) {

                    channel.sendMessageEmbeds(embedBuilder.build()).queue();

                } else if (chatReply && !addedToQueue) {
                    channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(TrackUtils.getTrackButtons()).queue();
                } else if (addedToQueue) {
                    event.replyEmbeds(embedBuilder.build()).queue();
                } else {
                    event.replyEmbeds(embedBuilder.build()).addActionRow(TrackUtils.getTrackButtons()).queue();
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                if (trackToLoad.startsWith("ytsearch:")) {
                    embedBuilder.setColor(EmbedUtils.getDefaultColor());
                    embedBuilder.setDescription("```Select a track```");

                    event.replyEmbeds(embedBuilder.build()).setActionRow(TrackUtils.createPlaylistTrackSelector(playlist).build()).setEphemeral(true).queue();
                    return;
                }

                playlist.getTracks().forEach(playlistTrack -> musicManager.getScheduler().queue(playlistTrack));
            }

            @Override
            public void noMatches() {
                event.replyEmbeds( EmbedUtils.getErrorEmbed("❌ **No track found**").build()).setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {

                String error = exception.getMessage() + " Caused by " + exception.getCause();

                if (exception.getMessage().equalsIgnoreCase("Unknown file format.")) error = "Unsupported Source.";
                event.replyEmbeds(EmbedUtils.getErrorEmbed("❗ **An error occurred:** `" + error + "`").build()).setEphemeral(true).queue();
            }

        });
    }

    public void stopTrack(Guild guild) {
        GuildMusicManager musicManager = getMusicManager(guild);
        musicManager.getAudioPlayer().stopTrack();
    }

    public void setPaused(Guild guild, boolean status) {
        GuildMusicManager musicManager = getMusicManager(guild);
        musicManager.getAudioPlayer().setPaused(status);
    }

    public boolean isTrackPause(Guild guild) {
        GuildMusicManager musicManager = getMusicManager(guild);
        return musicManager.getAudioPlayer().isPaused();
    }

    public boolean isPlaying(Guild guild) {
        GuildMusicManager musicManager = getMusicManager(guild);
        return musicManager.getAudioPlayer().getPlayingTrack() != null;
    }

    public void loop(Guild guild, boolean status) {
        GuildMusicManager musicManager = getMusicManager(guild);
        musicManager.getScheduler().setLoop(status);
    }

    public boolean isLoop(Guild guild) {
        GuildMusicManager musicManager = getMusicManager(guild);
        return musicManager.getScheduler().isLoop();
    }

}
