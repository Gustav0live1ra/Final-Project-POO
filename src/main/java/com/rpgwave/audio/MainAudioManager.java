package com.rpgwave.audio;

import com.rpgwave.core.GameState;

import static com.rpgwave.utils.Constants.GAMEPLAY_SOUND;
import static com.rpgwave.utils.Constants.MENU_SOUND;

public class MainAudioManager {

    private AudioPlayer menuMusic;
    private AudioPlayer gameplayMusic;

    private AudioPlayer currentMusic;

    public MainAudioManager() {
        menuMusic = new AudioPlayer(MENU_SOUND);
        gameplayMusic = new AudioPlayer(GAMEPLAY_SOUND);

        menuMusic.setVolume(-25.0f);
        gameplayMusic.setVolume(-30.0f);
    }

    public void onSceneChanged(GameState newState) {

        if (newState == GameState.PAUSED) {
            if (currentMusic != null) {
                currentMusic.pause();
            }
            return;
        }

        AudioPlayer nextMusic = null;
        switch (newState) {
            case MENU:
            case CHARACTER_SELECT:
                nextMusic = menuMusic;
                break;

            case PLAYING:
                nextMusic = gameplayMusic;
                break;

            case GAME_OVER:
                nextMusic = null;
                break;
        }

        if (currentMusic == nextMusic) {
            if (currentMusic != null) {
                currentMusic.resumeLoop();
            }
        } else {
            if (currentMusic != null) {
                currentMusic.stop();
            }

            currentMusic = nextMusic;

            if (currentMusic != null) {
                currentMusic.loop();
            }
        }
    }
}