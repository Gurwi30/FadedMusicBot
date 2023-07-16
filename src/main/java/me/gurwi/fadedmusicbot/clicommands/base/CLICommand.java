package me.gurwi.fadedmusicbot.clicommands.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CLICommand {

    private final String name;
    private final String description;

    public abstract void execute();

}
