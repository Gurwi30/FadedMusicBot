package me.gurwi.fadedmusicbot.utils;

public class Logger {

    public void info(String str) {
        String formattedString = String.format("[FadedMusicBot | %sINFO%s] - %s", ConsoleColors.CYAN_UNDERLINED, ConsoleColors.RESET, str);
        System.out.println(formattedString);
    }

    public void error(String str) {
        String formattedString = String.format("[FadedMusicBot | %sERROR%s] - %s", ConsoleColors.RED_UNDERLINED, ConsoleColors.RESET, str);
        System.out.println(formattedString);
    }

    public void warn(String str) {
        String formattedString = String.format("[FadedMusicBot | %sWARN%s] - %s", ConsoleColors.YELLOW_UNDERLINED, ConsoleColors.RESET, str);
        System.out.println(formattedString);
    }

}
