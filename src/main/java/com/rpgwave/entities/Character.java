package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.SpriteLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

//Classe base para qualquer personagem jogável.
//PESSOA B: implementar habilidades, level up, status effects, etc.

public abstract class Character extends Entity {

    protected final Stats stats;
    protected final InputHandler input;
    protected final BufferedImage sprite;

    // Controle de cooldown de ataque
    protected long lastAttackTime;

    public Character(double x, double y, int width, int height,
                     Stats stats, InputHandler input, String spritePath) {
        super(x, y, width, height);
        this.stats = stats;
        this.input = input;
        this.sprite = SpriteLoader.load(spritePath);
        this.lastAttackTime = 0;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
//        handleMovement();
        clampToBounds(worldWidth, worldHeight);
        handleAttack();
    }

    // Movimento é IGUAL pra qualquer personagem (WASD)
//    protected void handleMovement() {
//        double speed = stats.getSpeed();
//        if (input.isUp())    position.setY(position.getY() - speed);
//        if (input.isDown())  position.setY(position.getY() + speed);
//        if (input.isLeft())  position.setX(position.getX() - speed);
//        if (input.isRight()) position.setX(position.getX() + speed);
//    }

    protected void clampToBounds(int worldWidth, int worldHeight) {
        if (position.getX() < 0) position.setX(0);
        if (position.getY() < 0) position.setY(0);
        if (position.getX() + width > worldWidth)
            position.setX(worldWidth - width);
        if (position.getY() + height > worldHeight)
            position.setY(worldHeight - height);
    }

    // Ataque MUDA pra cada personagem, então é abstrato
    protected abstract void handleAttack();

    // Verifica se já passou o cooldown desde o último ataque
//   protected boolean canAttack() {
//        long now = System.currentTimeMillis();
//        long cooldownMs = (long) (stats.getAttackCooldown() * 1000);
//        return (now - lastAttackTime) >= cooldownMs;
//    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite,
                (int) position.getX(), (int) position.getY(),
                width, height, null);
    }

    public Stats getStats() { return stats; }
}