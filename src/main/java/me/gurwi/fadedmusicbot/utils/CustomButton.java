package me.gurwi.fadedmusicbot.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Objects;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum ButtonId {

    PAUSE_BUTTON(Button.secondary("FadedMusicBot_PauseButton", Emoji.fromUnicode("⏸️")));
    PAUSE_BUTTON(Button.secondary("FadedMusicBot_PauseButton", Emoji.fromUnicode("⏸️")));

    // CONTRUCTOR

    private final Button button;

    // METHODS

    public static Optional<ButtonId> matchButton(String id) {
        for (ButtonId b : values()) {
            if (!Objects.equals(b.getButton().getId(), id)) continue;
            return Optional.of(b);
        }

        return Optional.empty();
    }

}
