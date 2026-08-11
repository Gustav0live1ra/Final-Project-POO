package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class FlyingEnemy extends Enemy {

    // Layout dos sheets Bat_*.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 35;
    private static final int CROP_Y = 32;
    private static final int CROP_W = 45;
    private static final int CROP_H = 24;

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

        BufferedImage flyingSheet = SpriteLoader.load("/sprites/Bat_Flying.png");
        BufferedImage attackSheet = SpriteLoader.load("/sprites/Bat_Attack01.png");

        animations.put(State.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(flyingSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 100));
        animations.put(State.CHASE, new Animation(
                SpriteSheet.sliceRowCropped(flyingSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 70));
        animations.put(State.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(attackSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 50));
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
