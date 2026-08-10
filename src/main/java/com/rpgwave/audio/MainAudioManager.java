package com.rpgwave.audio;

import com.rpgwave.core.GameState;

import static com.rpgwave.utils.Constants.GAMEPLAY_SOUND;
import static com.rpgwave.utils.Constants.MENU_SOUND;

public class MainAudioManager {

    // 1. Variáveis para guardar nossas músicas (Pré-carregadas)
    private AudioPlayer menuMusic;
    private AudioPlayer gameplayMusic;

    private AudioPlayer currentMusic;

    public MainAudioManager() {
        // Pré-carregamos os áudios na memória UMA vez quando o jogo abre.
        menuMusic = new AudioPlayer(MENU_SOUND);
        gameplayMusic = new AudioPlayer(GAMEPLAY_SOUND);

        // Opcional: Ajustar volumes aqui se uma ficou mais alta que a outra
        menuMusic.setVolume(-25.0f);
        gameplayMusic.setVolume(-30.0f);
    }

    // Metodo que será chamado toda vez que a cena mudar
    public void onSceneChanged(GameState newState) {

        // Se entrou no pause, apenas pausa a música que estiver tocando
        if (newState == GameState.PAUSED) {
            if (currentMusic != null) {
                currentMusic.pause();
            }
            return;
        }

        //descobre qual deve ser a música da cena atual
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
            // a musica é a mesma, apenas retomamos de onde parou
            if (currentMusic != null) {
                currentMusic.resumeLoop();
            }
        } else {
            //se for diferente, para a musica atual
            if (currentMusic != null) {
                currentMusic.stop();
            }

            // troca a referência
            currentMusic = nextMusic;

            // inicia a música nova do zero.
            if (currentMusic != null) {
                currentMusic.loop(); // o loop() reinicia a faixa
            }
        }
    }
}