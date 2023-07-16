package me.gurwi.fadedmusicbot.clicommands;

import me.gurwi.fadedmusicbot.FadedMusicBot;
import me.gurwi.fadedmusicbot.clicommands.base.CLICommand;
import net.dv8tion.jda.api.JDA;

public class GetGuildsCommand extends CLICommand {

    public GetGuildsCommand() {
        super("guilds", "Get the list of all the guilds with the bot in it");
    }

    private final JDA jda = FadedMusicBot.getJda();

    @Override
    public void execute() {

        jda.getGuilds().forEach(guild -> System.out.println("› " + guild.getName()));

    }

}
