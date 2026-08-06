package com.rpgwave.entities;

import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Entity {

    private final Direction direction;
    private final int speed;
    private final int damage;
    private final BufferedImage sprite;

    public PlayerProjectile(
            double startX,
            double startY,
            Direction direction,
            int damage,
            int speed,
            String spritePath) {

        super(
                startX,
                startY,
                Constants.PROJECTILE_WIDTH,
                Constants.PROJECTILE_HEIGHT
        );

        this.direction = direction;
        this.damage = damage;
        this.speed = speed;
        this.sprite = null;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {

        switch (direction) {

            case UP:
                position.setY(position.getY() - speed);
                break;

            case DOWN:
                position.setY(position.getY() + speed);
                break;

            case LEFT:
                position.setX(position.getX() - speed);
                break;

            case RIGHT:
                position.setX(position.getX() + speed);
                break;
        }

        if (position.getX() + width < 0
                || position.getX() > worldWidth
                || position.getY() + height < 0
                || position.getY() > worldHeight) {

            active = false;
        }
    }

    @Override
    public void render(Graphics g) {

        g.setColor(java.awt.Color.YELLOW);

        g.fillOval(
                (int) position.getX(),
                (int) position.getY(),
                width,
                height
        );
    }

    public int getDamage() {
        return damage;
    }
}