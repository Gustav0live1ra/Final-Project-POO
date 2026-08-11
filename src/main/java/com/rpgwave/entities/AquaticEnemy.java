package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class AquaticEnemy extends Enemy {

    // Layout dos sheets Slime_*.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 33;
    private static final int CROP_Y = 38;
    private static final int CROP_W = 32;
    private static final int CROP_H = 21;

    public AquaticEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                1.2,
                5000,
                60,
                9999);
        this.placeholderColor = Color.BLUE;
        this.attackCooldownMs = 1500;

        BufferedImage idleSheet = SpriteLoader.load("/sprites/Slime_Idle.png");
        BufferedImage walkSheet = SpriteLoader.load("/sprites/Slime_Walk.png");
        BufferedImage attackSheet = SpriteLoader.load("/sprites/Slime_Attack01.png");

        animations.put(State.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(idleSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 150));
        animations.put(State.CHASE, new Animation(
                SpriteSheet.sliceRowCropped(walkSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 90));
        animations.put(State.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(attackSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 90));
    }

    @Override
    protected void performAttack() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(15);
        }
    }
}
