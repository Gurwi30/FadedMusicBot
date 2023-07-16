package me.gurwi.fadedmusicbot.clicommands;

import me.gurwi.fadedmusicbot.FadedMusicBot;
import me.gurwi.fadedmusicbot.clicommands.base.CLICommand;
import me.gurwi.fadedmusicbot.utils.ConsoleColors;

public class BotStopCommand extends CLICommand {

    public BotStopCommand() {
        super("stop", "Stop the bot");
    }

    @Override
    public void execute() {
        FadedMusicBot.getLogger().warn(ConsoleColors.YELLOW + "Stopping bot..." + ConsoleColors.RESET);
        System.exit(0);
    }

}
