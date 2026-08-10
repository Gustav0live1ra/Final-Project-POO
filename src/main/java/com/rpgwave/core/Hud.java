package com.rpgwave.core;

import com.rpgwave.entities.Character;
import com.rpgwave.entities.CharacterType;
import com.rpgwave.entities.Stats;

import java.awt.*;

public class Hud {

    public void render(
            Graphics g,
            Character player,
            CharacterType characterType,
            int currentWave,
            int enemiesRemaining,
            int viewWidth,
            int viewHeight) {

        if (player == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        try {

            // =========================
            // INFORMAÇÕES DO PERSONAGEM
            // =========================

            Stats stats = player.getStats();

            int x = 10;
            int y = 10;

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            g2.setColor(Color.WHITE);

            g2.drawString(
                    characterType.getDisplayName(),
                    x,
                    y + 13
            );

            // HP
            drawBar(
                    g2,
                    stats.getCurrentHealth(),
                    stats.getMaxHealth(),
                    x,
                    y + 20,
                    150,
                    10,
                    Color.RED
            );

            // Mana
            drawBar(
                    g2,
                    stats.getCurrentMana(),
                    stats.getMaxMana(),
                    x,
                    y + 35,
                    150,
                    10,
                    Color.BLUE
            );

            // =========================
            // WAVE
            // =========================

            int waveWidth = 125;
            int waveHeight = 48;

            int waveX =
                    viewWidth - waveWidth - 10;

            int waveY = 10;

            // Fundo transparente
            g2.setColor(
                    new Color(0, 0, 0, 140)
            );

            g2.fillRoundRect(
                    waveX,
                    waveY,
                    waveWidth,
                    waveHeight,
                    8,
                    8
            );

            // Borda discreta
            g2.setColor(
                    new Color(255, 255, 255, 100)
            );

            g2.drawRoundRect(
                    waveX,
                    waveY,
                    waveWidth,
                    waveHeight,
                    8,
                    8
            );

            // Texto da wave
            g2.setColor(Color.WHITE);

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            g2.drawString(
                    "WAVE " + currentWave,
                    waveX + 10,
                    waveY + 19
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            11
                    )
            );

            g2.drawString(
                    "Inimigos: " + enemiesRemaining,
                    waveX + 10,
                    waveY + 37
            );

        } finally {

            g2.dispose();
        }
    }

    private void drawBar(
            Graphics2D g,
            int current,
            int max,
            int x,
            int y,
            int width,
            int height,
            Color color) {

        // Fundo da barra
        g.setColor(
                new Color(0, 0, 0, 150)
        );

        g.fillRoundRect(
                x,
                y,
                width,
                height,
                5,
                5
        );

        // Calcula porcentagem
        double percentage =
                max > 0
                        ? (double) current / max
                        : 0;

        percentage =
                Math.max(
                        0,
                        Math.min(1, percentage)
                );

        int currentWidth =
                (int) (width * percentage);

        // Parte preenchida
        g.setColor(color);

        g.fillRoundRect(
                x,
                y,
                currentWidth,
                height,
                5,
                5
        );

        // Borda
        g.setColor(
                new Color(255, 255, 255, 80)
        );

        g.drawRoundRect(
                x,
                y,
                width,
                height,
                5,
                5
        );
    }
}