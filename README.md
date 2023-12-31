# FadedMusicBot
A simple discord music bot made because i was bored.

This bot is missing some feature like Spotify support or other platforms support for now but, don't worry i will add them in the future.
If you want you can contact me or you can open a pull request for some cool feature, i will be more than happy to implement it :)

## Features
- Supports `/` Commands
- Easy to setup
- Buttons to stop/skip/loop tracks
- No external keys needed
- Playlist support
- Fast loading of tracks
- Dropdown selection menu for search querys

## Supported Sources
- [x] YouTube
- [x] SoundCloud
- [x] Twitch streams
- [x] HTTP URLs
- [ ] Spotify

## Setup

### Discord Bot

1. Go on the [Discord Developer Portal](https://discord.com/developers/applications)
2. Create a new application (Call it how you want)
3. Select Bot section after creating the application
4. Enable every option in the **Privileged Gateway Intents** section
5. Press reset token and get the bot token
6. Save the token somewhere, it will be required for the next step!

### Installing FadedMusicBot

1. Install Java 17 or higher
2. Install the lastest release of FadedMusicBot from the releases page
3. Put the FadedMusicBot-X.X.X.jar in a folder
4. Install the start file for your OS.
   - Use the start.`bat` for windows
   - Use the start.`sh` for ubuntu.
     - If you are using ubuntu you will have to install `screen` using `sudo apt-get install screen`
5. Execute the start file, and configure the bot token in the `config.yml` file
6. Enjoy :)

## Discord Commands
- `/play [Track]` Plays a track from a link or title
- `/stop` Stops the current track
- `/loop [Current/Playlist]` Loops the current track or the current playlist/queue
- `/ping` Shows the bot current ping

## Cosole Commands
- `help` Shows the list of all the available commands
- `reload` Reloads the bot configuration and restarts it
- `stop` Stops the bot
- `guilds` Get the list of all the guilds with the bot in it
