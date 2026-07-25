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
                120,
                60,
                250);
        this.placeholderColor = Color.BLUE;
        this.attackCooldownMs = 1500;

        BufferedImage sheet = SpriteLoader.load("/sprites/slime-Sheet.png");

        animations.put(State.IDLE,   new Animation(SpriteSheet.sliceRow(sheet, 0, 8, 32, 25), 150));
        animations.put(State.CHASE,  new Animation(SpriteSheet.sliceRow(sheet, 0, 8, 32, 25), 90));
        animations.put(State.ATTACK, new Animation(SpriteSheet.sliceRow(sheet, 1, 8, 32, 25), 60));

    }

    @Override
    protected void performAttack() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(15);
        }
    }
}