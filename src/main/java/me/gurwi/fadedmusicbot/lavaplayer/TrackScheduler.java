package me.gurwi.fadedmusicbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import me.gurwi.fadedmusicbot.cache.ChannelsManager;
import me.gurwi.fadedmusicbot.cache.QueueManager;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import me.gurwi.fadedmusicbot.utils.TrackUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer audioPlayer;
    private final String guildId;
    @Getter
    private final BlockingQueue<AudioTrack> queue;

    private final ChannelsManager channelsManager = ChannelsManager.getInstance();

    @Getter
    @Setter
    private boolean loop;

    public TrackScheduler(AudioPlayer audioPlayer, String guildId) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
        this.guildId = guildId;
    }

    public void queue(AudioTrack track) {
        if (!audioPlayer.startTrack(track, true)) queue.offer(track);
    }

    public void nextTrack() {
        audioPlayer.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if (loop && endReason.mayStartNext) {
            audioPlayer.playTrack(track.makeClone());
            return;
        }

        if (endReason.mayStartNext) {
             nextTrack();

            AudioTrack nextTrack = audioPlayer.getPlayingTrack();

            if (channelsManager.getLastChatMessageChannel().containsKey(guildId) && nextTrack != null) {

                User user = QueueManager.getInstance().getWhoQueued(nextTrack);

                EmbedBuilder embedBuilder = new EmbedBuilder();

                embedBuilder.setAuthor("Queued by " + user.getEffectiveName(), user.getAvatarUrl(), user.getAvatarUrl());
                embedBuilder.setColor(EmbedUtils.getDefaultColor());
                embedBuilder.setTitle(EmbedUtils.getTITLE());

                Duration duration = Duration.ofMillis(nextTrack.getDuration());
                long seconds = duration.getSeconds();

                long HH = seconds / 3600;
                long MM = (seconds % 3600) / 60;
                long SS = seconds % 60;

                String timeInHHMMSS = String.format("%02d:%02d:%02d", HH, MM, SS);

                MessageEmbed.Field trackNameField = new MessageEmbed.Field("**🎵 Now playing **", "`" + nextTrack.getInfo().title + "`", false);
                MessageEmbed.Field trackDurationField = new MessageEmbed.Field("**ℹ️ Other Info**", "``` Duration: " + timeInHHMMSS + "\n" +
                        " Author: " + nextTrack.getInfo().author + " ```", false);

                embedBuilder.addField(trackNameField);
                embedBuilder.addField(trackDurationField);

                embedBuilder.setThumbnail("https://img.youtube.com/vi/" + nextTrack.getInfo().identifier + "/hqdefault.jpg");

                channelsManager.getGuildChat(guildId).sendMessageEmbeds(embedBuilder.build()).addActionRow(TrackUtils.getTrackButtons()).queue();

                QueueManager.getInstance().getQueueTrack().remove(nextTrack.getIdentifier());
            }

        }

    }

}
