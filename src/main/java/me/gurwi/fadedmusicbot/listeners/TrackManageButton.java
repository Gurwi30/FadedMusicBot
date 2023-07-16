package me.gurwi.fadedmusicbot.listeners;

import me.gurwi.fadedmusicbot.commands.StopCommand;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.CustomButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackManageButton extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Button button = event.getButton();
        Optional<CustomButton> buttonId = CustomButton.matchButton(button);
        AudioPlayer audioPlayer = AudioPlayer.getInstance();

        if (buttonId.isEmpty()) return;

        switch (buttonId.get()) {

            case PAUSE_BUTTON:
                audioPlayer.setPaused(event.getGuild(), true);
                event.editButton(CustomButton.PLAY_BUTTON.getButton()).queue();
                break;

            case PLAY_BUTTON:
                audioPlayer.setPaused(event.getGuild(), false);
                event.editButton(CustomButton.PAUSE_BUTTON.getButton()).queue();
                break;

            case STOP_BUTTON:
                StopCommand.stopTrack(event, true, event.getChannel());
                List<LayoutComponent> itemComponents = new ArrayList<>();
                event.getMessage().getComponents().forEach(msgButton -> {
                    LayoutComponent editedButton = msgButton.asDisabled();
                    itemComponents.add(editedButton);
                });

                event.editComponents(itemComponents).queue();
                break;

        }

    }


}
