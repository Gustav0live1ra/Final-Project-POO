package com.rpgwave.entities;

import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.Vector2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Projectile extends Entity {

    private final Vector2D velocity;
    private final BufferedImage sprite;

    public Projectile(double startX, double startY,
                      int targetX, int targetY, int speed) {
        super(startX, startY, Constants.PROJECTILE_WIDTH, Constants.PROJECTILE_HEIGHT);

        this.sprite = SpriteLoader.load(Constants.PROJECTILE_SPRITE);

        Vector2D direction = new Vector2D(
                targetX - startX,
                targetY - startY
        ).normalize();

        this.velocity = direction.multiply(speed);
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        position = position.add(velocity);

        if (position.getX() + width < 0 || position.getX() > worldWidth ||
                position.getY() + height < 0 || position.getY() > worldHeight) {
            active = false;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite,
                (int) position.getX(), (int) position.getY(),
                width, height, null);
    }
}