package me.gurwi.fadedmusicbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class TrackUtils {

    @Getter
    private static final String trackSelectorId = "fadedmusicbot_trackselector";

    public static StringSelectMenu.Builder createPlaylistTrackSelector(AudioPlaylist playlist) {

        StringSelectMenu.Builder selectionMenuBuilder = StringSelectMenu.create(trackSelectorId);
        selectionMenuBuilder.setPlaceholder("Select a track");

        playlist.getTracks().forEach(audioTrack -> selectionMenuBuilder.addOption(audioTrack.getInfo().title, audioTrack.getInfo().uri, audioTrack.getInfo().author));

        return selectionMenuBuilder;
    }

    public static List<ItemComponent> getTrackButtons() {
        List<ItemComponent> trackButtons = new ArrayList<>();
        trackButtons.add(CustomButton.PAUSE_BUTTON.getButton());
        trackButtons.add(CustomButton.STOP_BUTTON.getButton());

        return trackButtons;
    }

}
