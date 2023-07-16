package me.gurwi.fadedmusicbot.clicommands.base;

import me.gurwi.fadedmusicbot.clicommands.BotStopCommand;
import me.gurwi.fadedmusicbot.clicommands.CLIHelpCommand;
import me.gurwi.fadedmusicbot.clicommands.GetGuildsCommand;
import me.gurwi.fadedmusicbot.clicommands.ReloadConfigCommand;
import me.gurwi.fadedmusicbot.clicommands.base.CLICommand;
import me.gurwi.fadedmusicbot.utils.ConsoleColors;
import me.gurwi.fadedmusicbot.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLICommandHandler {

    private final Scanner scanner;
    private final Logger logger;
    private final Map<String, CLICommand> cliCommandMap = new HashMap<>();

    public CLICommandHandler(Scanner scanner, Logger logger) {
        this.scanner = scanner;
        this.logger = logger;

        registerCommand(new GetGuildsCommand());
        registerCommand(new ReloadConfigCommand());
        registerCommand(new BotStopCommand());

        registerCommand(new CLIHelpCommand(cliCommandMap));
    }

    private void registerCommand(CLICommand command) {
        cliCommandMap.put(command.getName(), command);
    }

    public void run() {

        while (true) {
            System.out.print("[FadedMusicBot | User Input] » ");
            String input = scanner.nextLine().toLowerCase();

            if (!cliCommandMap.containsKey(input)) {
                logger.error(ConsoleColors.RED_UNDERLINED + "Command not found!" + ConsoleColors.RESET + " Use the command " + ConsoleColors.WHITE_UNDERLINED + "help" + ConsoleColors.RESET + " to get the list of all the available commands!");
            } else {
                cliCommandMap.get(input).execute();
            }

        }

    }

}
