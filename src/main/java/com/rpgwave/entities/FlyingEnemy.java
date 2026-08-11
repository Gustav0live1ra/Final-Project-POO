package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class FlyingEnemy extends Enemy {

    private boolean dashing = false;
    private double dashDirX, dashDirY;
    private final double dashSpeed = 8.0;
    private long dashStartTime;
    private final long dashDurationMs = 300;

    public FlyingEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                3.0,
                5000,
                50,
                9999);
        this.placeholderColor = Color.CYAN;
        this.attackCooldownMs = 1500;

        BufferedImage sheet = SpriteLoader.load("/sprites/flying_eye_sheet.png");
        animations.put(State.IDLE, new Animation(SpriteSheet.sliceRow(sheet, 0, 12, 192, 192), 80));
        animations.put(State.CHASE, new Animation(SpriteSheet.sliceRow(sheet, 0, 12, 192, 192), 60));
        animations.put(State.ATTACK, new Animation(SpriteSheet.sliceRow(sheet, 1, 8, 192, 192), 50));
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        if (dashing) {
            position.setX(position.getX() + dashDirX * dashSpeed);
            position.setY(position.getY() + dashDirY * dashSpeed);

            if (System.currentTimeMillis() - dashStartTime >= dashDurationMs) {
                dashing = false;
            }
        } else {
            super.update(worldWidth, worldHeight);
        }
    }

    @Override
    protected void performAttack() {
        double dx = target.getCenterX() - getCenterX();
        double dy = target.getCenterY() - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > 0) {
            dashDirX = dx / dist;
            dashDirY = dy / dist;
        }

        dashing = true;
        dashStartTime = System.currentTimeMillis();

        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(20);
        }
    }
}