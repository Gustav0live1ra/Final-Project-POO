package com.rpgwave.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class AudioPlayer {

    private Clip clip;

    // Construtor que recebe o caminho do arquivo de áudio
    public AudioPlayer(String filePath) {
        try {
            // Pega o arquivo de áudio da pasta de resources
            URL url = getClass().getResource(filePath);

            if (url == null) {
                System.err.println("Arquivo de áudio não encontrado: " + filePath);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            // Pega um clip de som e abre o fluxo de áudio
            clip = AudioSystem.getClip();
            clip.open(audioIn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Toca o som uma vez (ideal para efeitos sonoros)
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Volta o som pro começo
            clip.start();
        }
    }

    // Toca o som em loop (trilha sonora)
    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // encerra o som
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Controle de volume (valor em decibéis. Ex: -10.0f diminui o volume)
    public void setVolume(float decibeis) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(decibeis);
        }
    }

    // Apenas pausa o som na posição atual
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Retoma o loop de onde parou (sem voltar para o frame 0)
    public void resumeLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
