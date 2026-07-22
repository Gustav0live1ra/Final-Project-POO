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

    private Character player;  //  (polimorfismo)
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

        // Cria o personagem escolhido no menu!
//        player = CharacterFactory.create(
//                chosenCharacter, startX, startY, input, projectiles
//        );
    }

    @Override
    public void onExit() {
        projectiles.clear();
    }

    @Override
    public void update() {
        player.update(worldWidth, worldHeight);

        for (Projectile p : projectiles) {
            p.update(worldWidth, worldHeight);
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, worldWidth, worldHeight);

        player.render(g);

        for (Projectile p : projectiles) {
            p.render(g);
        }

        // HUD provisória (Pessoa D vai fazer bonita)
        g.setColor(Color.WHITE);
        g.drawString("Personagem: " + chosenCharacter.getDisplayName(), 10, 20);
//        g.drawString("HP: " + player.getStats().getCurrentHealth() +
//                "/" + player.getStats().getMaxHealth(), 10, 40);
    }
}