package me.gurwi.fadedmusicbot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenersManager {

    private final JDA jda;

    public ListenersManager(JDA jda) {
        this.jda = jda;

        registerListener(new TrackManageButton());
        registerListener(new TrackSelector());
        registerListener(new BotDisconnect());
    }

    private void registerListener(ListenerAdapter listener) {
        jda.addEventListener(listener);
    }

}
