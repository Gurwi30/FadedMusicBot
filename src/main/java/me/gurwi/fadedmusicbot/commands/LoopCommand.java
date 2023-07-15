package me.gurwi.fadedmusicbot.commands;

import me.gurwi.fadedmusicbot.commands.base.BotCommand;
import me.gurwi.fadedmusicbot.lavaplayer.AudioPlayer;
import me.gurwi.fadedmusicbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class LoopCommand extends BotCommand {

    public LoopCommand() {
        super("loop", "Loop your favorate song!");
    }

    private final AudioPlayer audioPlayer = AudioPlayer.getInstance();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member member = event.getMember();
        Guild guild = event.getGuild();
        GuildVoiceState botVoiceState = event.getGuild().getSelfMember().getVoiceState();

        if (!audioPlayer.isPlaying(guild) || !Objects.requireNonNull(botVoiceState).inAudioChannel()) {
            event.replyEmbeds(EmbedUtils.getErrorEmbed("` ❌ The bot isn't playing any track`").build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(EmbedUtils.getDefaultColor());
        embedBuilder.setAuthor("Sent by " + member.getEffectiveName(), member.getAvatarUrl(), member.getUser().getAvatarUrl());

        if (audioPlayer.isLoop(guild)) {
            audioPlayer.loop(guild, false);

            embedBuilder.setDescription("`ℹ️ Track reapeating is now disabled.`");
            event.replyEmbeds(embedBuilder.build()).queue();

        } else {
            audioPlayer.loop(guild, true);

            embedBuilder.setDescription("`🔁 The current track will now be reapeated.`");
            event.replyEmbeds(embedBuilder.build()).queue();

        }

    }

}
