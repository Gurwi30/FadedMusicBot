package me.gurwi.fadedmusicbot.config;

import lombok.Getter;
import net.bobolabs.config.Configuration;
import net.bobolabs.config.ConfigurationLoader;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Getter
@SuppressWarnings({"unused"})
public class YamlConfig {

    private final Class<?> main;
    private final String directory;
    private final boolean replace;

    private Configuration config;

    // METHODS

    public void loadConfig() {
        File dataFolder = new File(getProgramPath() + File.separator);

        config = ConfigurationLoader
                .fromFile(dataFolder, "config.yml")
                .setDefaultResource("config.yml")
                .load();

    }

    public void reloadConfig() {
        config.reload();
    }

    //

    // UTILS

    private String getProgramPath() {
        URL url = main.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
        return new File(jarPath).getParentFile().getPath();
    }

    // CONSTRUCTOR

    public YamlConfig(Class<?> main, String directory, boolean replace, boolean load) {
        this.main = main;
        this.directory = directory;
        this.replace = replace;
        if (load) loadConfig();
    }

}
