package me.gurwi.fadedmusicbot;

import me.gurwi.fadedmusicbot.commands.base.BotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LoopCommand extends BotCommand {

    public LoopCommand() {
        super("loop", "Loop your favorate song!");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        
    }

}
