package me.gurwi.fadedmusicbot.clicommands;

import me.gurwi.fadedmusicbot.clicommands.base.CLICommand;

import java.util.Map;

public class CLIHelpCommand extends CLICommand {

    private final Map<String, CLICommand> commands;

    public CLIHelpCommand(Map<String, CLICommand> commands) {
        super("help", "Shows the list of all the available commands");
        this.commands = commands;
    }

    @Override
    public void execute() {

        System.out.println();
        System.out.println(" ◯ Available commands:");
        System.out.println();
        commands.forEach((s, command) -> System.out.println(" → " + s + " (" + command.getDescription() + ")"));
        System.out.println();

    }

}
