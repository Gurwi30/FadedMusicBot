package me.gurwi.fadedmusicbot;

import lombok.Getter;
import me.gurwi.fadedmusicbot.commands.base.CommandsHandler;
import me.gurwi.fadedmusicbot.listeners.ListenersManager;
import me.gurwi.fadedmusicbot.listeners.TrackManageButton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class FadedMusicBot {

    @Getter
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Getter
    private static JDA jda;
    private static final Logger logger = Logger.getLogger("FadedMusicBot");

    public static void main(String[] args) throws InterruptedException {

        jda = JDABuilder.createLight("MTEyNTgwMDI1NzM2NDY5NzE3OA.GmyjWF.QyoucwMSKGr7px1a7T6oc3qcLKzFiaF6lIT-Nc", Collections.emptyList())
                .setMemberCachePolicy(MemberCachePolicy.VOICE)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS)
                .build()
                .awaitReady();

        // REGISTER COMMANDS & LISTENERS

        new CommandsHandler(jda);
        new ListenersManager(jda);

        // RICH PRESENCE

        jda.getPresence().setActivity(Activity.streaming("Feet Pics", "https://www.youtube.com/watch?v=RcQut-ACe70"));
        jda.getPresence().setStatus(OnlineStatus.IDLE);

    }

}
