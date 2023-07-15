package me.gurwi.fadedmusicbot.commands.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
@RequiredArgsConstructor
public abstract class ICommand {

    private final String name;
    private final String description;
    private CMDOption[] cmdOptions;

    public ICommand(String name, String description, CMDOption... cmdOptions) {
        this.name = name;
        this.description = description;
        this.cmdOptions = cmdOptions;
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
