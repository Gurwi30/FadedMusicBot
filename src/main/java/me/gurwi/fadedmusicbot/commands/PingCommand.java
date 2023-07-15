package me.gurwi.fadedmusicbot.commands;

import me.gurwi.fadedmusicbot.commands.base.BotCommand;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends BotCommand {

    public PingCommand() {
        super("ping", "Check bot latency");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        JDA bot = event.getJDA();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(EmbedUtils.getDefaultColor());
        embedBuilder.setDescription("`\uD83C\uDFD3API Latency is " + bot.getGatewayPing() + "ms`");
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

    }

}
