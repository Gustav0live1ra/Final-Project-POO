package com.rpgwave.core;

import com.rpgwave.entities.*;
import com.rpgwave.entities.Character;
import com.rpgwave.utils.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayingScene implements GameScene {

    private final InputHandler input;
    private final int worldWidth;
    private final int worldHeight;
    private final CharacterType chosenCharacter;

    private Character player;
    private Player testPlayer; // TODO: remover quando Warrior/Archer/Mage estiverem prontos
    private WaveManager waveManager;
    private CopyOnWriteArrayList<Projectile> projectiles;

    public PlayingScene(InputHandler input, int worldWidth, int worldHeight,
                        CharacterType chosenCharacter) {
        this.input = input;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.chosenCharacter = chosenCharacter;
    }

    @Override
    public void onEnter() {
        projectiles = new CopyOnWriteArrayList<>();

        double startX = worldWidth / 2.0;
        double startY = worldHeight / 2.0;

        // TODO: quando CharacterFactory/Warrior/Archer/Mage estiverem prontos,
        // troca essas 2 linhas por:
        // player = CharacterFactory.create(chosenCharacter, startX, startY, input, projectiles);
        testPlayer = new Player(startX, startY, input);

        waveManager = new WaveManager(testPlayer, worldWidth, worldHeight);
    }

    @Override
    public void onExit() {
        projectiles.clear();
    }
        @Override
        public void update() {
            testPlayer.update(worldWidth, worldHeight);
            waveManager.update(worldWidth, worldHeight);

            for (Projectile p : projectiles) {
                p.update(worldWidth, worldHeight);
            }
            projectiles.removeIf(p -> !p.isActive());

            // TEMPORÁRIO: ataque de teste por clique, até Warrior/Archer/Mage existirem
            if (input.consumeMouseClick()) {
                double attackRange = 60;
                for (Enemy e : waveManager.getActiveEnemies()) {
                    double dx = e.getCenterX() - testPlayer.getCenterX();
                    double dy = e.getCenterY() - testPlayer.getCenterY();
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist < attackRange) {
                        e.takeDamage(50);
                    }
                }
            }
        }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, worldWidth, worldHeight);

        testPlayer.render(g);
        waveManager.render(g);

        for (Projectile p : projectiles) {
            p.render(g);
        }

        g.setColor(Color.WHITE);
        g.drawString("Wave: " + waveManager.getCurrentWave(), 10, 20);
        g.drawString("HP: " + testPlayer.getHealth(), 10, 40);
    }
}