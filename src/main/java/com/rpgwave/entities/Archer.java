package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Animation;
import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.image.BufferedImage;

public class Archer extends Character {

    // Layout dos sheets Archer_*.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 38;
    private static final int CROP_Y = 36;
    private static final int CROP_W = 41;
    private static final int CROP_H = 26;

    private static final int RENDER_WIDTH = 100;
    private static final int RENDER_HEIGHT = 64;

    public Archer(double x, double y, InputHandler input) {

        super(
                x,
                y,
                RENDER_WIDTH,
                RENDER_HEIGHT,
                new Stats(100, 80, 25, 15, 5.5),
                input,
                Constants.PLAYER_SPRITE
        );

        addSkill(new Skill("Flecha Perfurante", 35, 15));

        BufferedImage idleSheet = SpriteLoader.load("/sprites/Archer_Idle.png");
        BufferedImage walkSheet = SpriteLoader.load("/sprites/Archer_Walk.png");
        BufferedImage attackSheet = SpriteLoader.load("/sprites/Archer_Attack01.png");

        animations.put(AnimState.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(idleSheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 150));
        animations.put(AnimState.WALK, new Animation(
                SpriteSheet.sliceRowCropped(walkSheet, 0, 8, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 90));
        animations.put(AnimState.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(attackSheet, 0, 9, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 70));

        BufferedImage arrowSprite = SpriteLoader.load("/sprites/Arrow_Projectile.png");
        projectileFrames = new BufferedImage[]{ arrowSprite.getSubimage(6, 11, 23, 11) };
        projectileBaseAngleDeg = 0;
        projectileWidth = 34;
        projectileHeight = 16;
    }

    @Override
    protected void handleAttack() {
    }
}
