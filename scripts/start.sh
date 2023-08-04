screen -list | grep "FadedMusicBot" && echo "[Error] This bot is already online" || screen -S FadedMusicBot java -Dnogui=true -jar FadedMusicBot-1.0.1.jar
