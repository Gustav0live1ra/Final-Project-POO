package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class GoblinEnemy extends Enemy {

    public GoblinEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                2.0,
                350,
                50,
                500);
        this.placeholderColor = Color.GREEN;
        this.attackCooldownMs = 800;

        BufferedImage sheet = SpriteLoader.load("/sprites/goblin_spritesheet.png");

        animations.put(State.IDLE, new Animation(SpriteSheet.sliceRow(sheet, 0, 6, 80, 80), 150));
        animations.put(State.CHASE, new Animation(SpriteSheet.sliceRow(sheet, 1, 8, 80, 80), 100));
        animations.put(State.ATTACK, new Animation(SpriteSheet.sliceRow(sheet, 4, 8, 80, 80), 100));
    }

    @Override
    protected void performAttack() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(10);
        }
    }
}