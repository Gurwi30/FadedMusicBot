package me.gurwi.fadedmusicbot.clicommands;

import lombok.SneakyThrows;
import me.gurwi.fadedmusicbot.FadedMusicBot;
import me.gurwi.fadedmusicbot.clicommands.base.CLICommand;
import me.gurwi.fadedmusicbot.config.YamlConfig;
import me.gurwi.fadedmusicbot.utils.ConsoleColors;
import me.gurwi.fadedmusicbot.utils.Logger;

public class ReloadConfigCommand extends CLICommand {

    public ReloadConfigCommand() {
        super("reload", "Reload bot configurationh");
    }

    private final YamlConfig botConfig = FadedMusicBot.getBotConfig();
    private final Logger logger = FadedMusicBot.getLogger();

    @SneakyThrows
    @Override
    public void execute() {

        logger.warn(ConsoleColors.YELLOW + "Started bot config reload..." + ConsoleColors.RESET);
        long start = System.currentTimeMillis();

        FadedMusicBot.getJda().shutdown();
        botConfig.getConfig().reload();
        FadedMusicBot.startBot();

        logger.info(String.format("%sConfig reload finished in %s%s%sms%s", ConsoleColors.CYAN, ConsoleColors.WHITE_UNDERLINED, (System.currentTimeMillis() - start), ConsoleColors.CYAN, ConsoleColors.RESET));
    }

}
