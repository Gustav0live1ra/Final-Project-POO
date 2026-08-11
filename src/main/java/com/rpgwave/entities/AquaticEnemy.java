package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class AquaticEnemy extends Enemy {

    public AquaticEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                1.2,
                5000,
                60,
                9999);
        this.placeholderColor = Color.BLUE;
        this.attackCooldownMs = 1500;

        BufferedImage sheet = SpriteLoader.load("/sprites/slime-Sheet.png");
        animations.put(State.IDLE,   new Animation(SpriteSheet.sliceRow(sheet, 0, 6, 32, 75), 150));
        animations.put(State.CHASE,  new Animation(SpriteSheet.sliceRow(sheet, 0, 6, 32, 75), 90));
        animations.put(State.ATTACK, new Animation(SpriteSheet.sliceRow(sheet, 0, 6, 32, 75), 60));
    }

    @Override
    protected void performAttack() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(15);
        }
    }
}