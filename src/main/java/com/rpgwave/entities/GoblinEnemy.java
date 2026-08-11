package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class GoblinEnemy extends Enemy {

    // Layout dos sheets Orc_*.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 34;
    private static final int CROP_Y = 31;
    private static final int CROP_W = 41;
    private static final int CROP_H = 33;

    public GoblinEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                2.0,
                5000,
                50,
                9999);
        this.placeholderColor = Color.GREEN;
        this.attackCooldownMs = 800;

        BufferedImage idleSheet = SpriteLoader.load("/sprites/Orc_Idle.png");
        BufferedImage walkSheet = SpriteLoader.load("/sprites/Orc_Walk.png");
        BufferedImage attackSheet = SpriteLoader.load("/sprites/Orc_Attack01.png");

        animations.put(State.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(idleSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 150));
        animations.put(State.CHASE, new Animation(
                SpriteSheet.sliceRowCropped(walkSheet, 0, 8, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 100));
        animations.put(State.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(attackSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 100));
    }

    @Override
    protected void performAttack() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(10);
        }
    }
}
