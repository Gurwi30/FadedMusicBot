package me.gurwi.fadedmusicbot.commands.base;

import me.gurwi.fadedmusicbot.commands.LoopCommand;
import me.gurwi.fadedmusicbot.commands.PingCommand;
import me.gurwi.fadedmusicbot.commands.PlayCommand;
import me.gurwi.fadedmusicbot.commands.StopCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.*;

public class CommandsHandler extends ListenerAdapter {

    private final Map<String, BotCommand> commandMap = new HashMap<>();
    private final JDA jda;

    public CommandsHandler(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);

        register(new PlayCommand());
        register(new StopCommand());
        register(new LoopCommand());
        register(new PingCommand());

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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String command = event.getName().toLowerCase();
        if (!commandMap.containsKey(command)) return;
        commandMap.get(command).execute(event);

    }

}
