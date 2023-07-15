package me.gurwi.fadedmusicbot.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum CustomButton {

    STOP_BUTTON(Button.danger("FadedMusicBot_StopButton", Emoji.fromUnicode("⏹️"))),
    PLAY_BUTTON(Button.secondary("FadedMusicBot_PlayButton", Emoji.fromUnicode("▶️"))),
    PAUSE_BUTTON(Button.secondary("FadedMusicBot_PauseButton", Emoji.fromUnicode("⏸️")));

    // CONTRUCTOR

    private final Button button;

    // METHODS

    public static Optional<CustomButton> matchButton(Button button) {
        for (CustomButton b : values()) {
            if (!b.getButton().equals(button)) continue;
            return Optional.of(b);
        }

        return Optional.empty();
    }

}
