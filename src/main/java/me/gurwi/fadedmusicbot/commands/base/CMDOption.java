package me.gurwi.fadedmusicbot.commands.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@Getter
@RequiredArgsConstructor
public class CMDOption {

    private final OptionType optionType;
    private final String name;
    private final String description;
    private final boolean required;

}
