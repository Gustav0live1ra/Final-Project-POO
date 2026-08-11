package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Animation;
import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.image.BufferedImage;

public class Mage extends Character {

    // Layout dos sheets Wizard_*.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 41;
    private static final int CROP_Y = 34;
    private static final int CROP_W = 25;
    private static final int CROP_H = 27;

    private static final int RENDER_WIDTH = 60;
    private static final int RENDER_HEIGHT = 64;

    public Mage(double x, double y, InputHandler input) {

        super(
                x,
                y,
                RENDER_WIDTH,
                RENDER_HEIGHT,
                new Stats(80, 150, 35, 10, 3.5),
                input,
                Constants.PLAYER_SPRITE
        );
        addSkill(new Skill("Bola de fogo", 50, 30));

        BufferedImage idleSheet = SpriteLoader.load("/sprites/Wizard_Idle.png");
        BufferedImage walkSheet = SpriteLoader.load("/sprites/Wizard_Walk.png");
        BufferedImage attackSheet = SpriteLoader.load("/sprites/Wizard_Attack01.png");

        animations.put(AnimState.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(idleSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 150));
        animations.put(AnimState.WALK, new Animation(
                SpriteSheet.sliceRowCropped(walkSheet, 0, 8, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 90));
        animations.put(AnimState.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(attackSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 70));

        BufferedImage fireballSheet = SpriteLoader.load("/sprites/Fireball_Projectile.png");
        projectileFrames = SpriteSheet.sliceRowCropped(
                fireballSheet, 0, 4, CELL_SIZE, CELL_SIZE, 36, 42, 28, 19);
        projectileFrameDurationMs = 80;
        projectileBaseAngleDeg = 0;
        projectileWidth = 34;
        projectileHeight = 23;
    }

    @Override
    protected void handleAttack() {
    }
}
