package me.gurwi.fadedmusicbot.commands.base;

import me.gurwi.fadedmusicbot.commands.CockCommand;
import me.gurwi.fadedmusicbot.commands.PlayCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.*;

public class CMDHandler {

    private final Map<String, BotCommand> commandMap = new HashMap<>();
    private final JDA jda;

    public CMDHandler(JDA jda) {
        this.jda = jda;

        register(new PlayCommand());
        register(new CockCommand());

        registerSlashCommands();
    }

    private void register(BotCommand command) {
        commandMap.put(command.getName(), command);
        System.out.println("| Registered " + command.getName() + " command!");
    }

    @SuppressWarnings("all")
    private void registerSlashCommands() {

        Set<SlashCommandData> slashCommands = new HashSet<>();

        commandMap.forEach((name, command) -> {
            SlashCommandData slashCommandData = Commands.slash(command.getName().toLowerCase(), command.getDescription()).setGuildOnly(true);

            if (command.getCmdOptions() != null) {
                for (CMDOption cmdOption : command.getCmdOptions()) {
                    slashCommandData.addOption(cmdOption.getOptionType(), cmdOption.getName().toLowerCase(), cmdOption.getDescription(), cmdOption.isRequired());
                }
            }

            slashCommands.add(slashCommandData);
            System.out.println("| Registered slash command for " + command.getName());
        });

        jda.updateCommands().addCommands(slashCommands).queue();
    }

}
