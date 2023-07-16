package me.gurwi.fadedmusicbot;

import lombok.Getter;
import me.gurwi.fadedmusicbot.clicommands.base.CLICommandHandler;
import me.gurwi.fadedmusicbot.commands.base.CommandsHandler;
import me.gurwi.fadedmusicbot.config.ConfigManager;
import me.gurwi.fadedmusicbot.config.YamlConfig;
import me.gurwi.fadedmusicbot.listeners.ListenersManager;
import me.gurwi.fadedmusicbot.utils.BasicFunctions;
import me.gurwi.fadedmusicbot.utils.ConsoleColors;
import me.gurwi.fadedmusicbot.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FadedMusicBot {

    @Getter
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Getter
    private static final Logger logger = new Logger();

    private static final Scanner scanner = new Scanner(System.in);

    @Getter
    private static JDA jda;
    @Getter
    private static YamlConfig botConfig;

    public static void main(String[] args) throws InterruptedException {

        System.out.println(BasicFunctions.startUpText());

        // LOAD CONFIG

        logger.info(ConsoleColors.CYAN + "Loading configuration..." + ConsoleColors.RESET);
        botConfig = new YamlConfig(FadedMusicBot.class, "config.yml", false, true);

        // LOAD BOT

        logger.info(ConsoleColors.CYAN + "Starting discord bot..." + ConsoleColors.RESET);
        startBot();

        logger.info(ConsoleColors.CYAN + "BOT STARTED SUCCESSFULLY" + ConsoleColors.RESET);

        // USER INPUT

        new CLICommandHandler(scanner, logger).run();

    }

    public static void startBot() throws InterruptedException {
        while (true) {
            try {
                jda = JDABuilder.createLight(ConfigManager.TOKEN.getString(), Collections.emptyList())
                        .setMemberCachePolicy(MemberCachePolicy.VOICE)
                        .enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                        .enableCache(CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS)
                        .build()
                        .awaitReady();

                break;

            } catch (IllegalArgumentException | InvalidTokenException e) {
                logger.error(ConsoleColors.RED_BRIGHT + "Invalid or empty token!" + ConsoleColors.RESET);
                String botToken = getInput(scanner, "INSERT BOT TOKEN");

                botConfig.getConfig().set("Bot-Token", botToken);
                botConfig.getConfig().save();
                botConfig.getConfig().reload();
            }
        }

        // REGISTER COMMANDS & LISTENERS

        logger.info(ConsoleColors.CYAN + "Loading Commands & Listeners..." + ConsoleColors.RESET);
        new CommandsHandler(jda);
        new ListenersManager(jda);

        // RICH PRESENCE

        OnlineStatus onlineStatus = OnlineStatus.IDLE;

        Activity.ActivityType activity = Activity.ActivityType.STREAMING;
        String activityName = "Goofy aah songs";
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

        if (BasicFunctions.isValidEnum(OnlineStatus.class, ConfigManager.BOT_STATUS.getString())) {
            onlineStatus = OnlineStatus.valueOf(ConfigManager.BOT_STATUS.getString().toUpperCase());
        } else {
            logger.error(ConsoleColors.RED_BRIGHT + "Invalid status type, using the default one!" + ConsoleColors.RESET);
        }

        if (BasicFunctions.isValidEnum(Activity.ActivityType.class, ConfigManager.BOT_ACTIVITY.getString())) {
            activity = Activity.ActivityType.valueOf(ConfigManager.BOT_ACTIVITY.getString().toUpperCase());
        } else {
            logger.error(ConsoleColors.RED_BRIGHT + "Invalid activity type, using the default one!" + ConsoleColors.RESET);
        }


        logger.info(ConsoleColors.CYAN + "Loading bot Activity & Status..." + ConsoleColors.RESET);

        jda.getPresence().setStatus(onlineStatus);
        jda.getPresence().setActivity(Activity.of(activity, activityName, url));

    }

    private static String getInput(Scanner scanner, String prompt) {
        sendPromptMessage(prompt);
        return scanner.nextLine();
    }

    private static void sendPromptMessage(String prompt) {
        System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + " " + prompt + " " + ConsoleColors.WHITE + " » ");
    }

}
