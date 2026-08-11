package com.rpgwave.entities;
import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Animation;
import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import com.rpgwave.utils.SpriteSheet;
import java.awt.image.BufferedImage;

public class Warrior extends Character {

    // Layout do sheet Soldier.png (Tiny RPG Character Asset Pack, Zerie):
    private static final int CELL_SIZE = 100;
    private static final int CROP_X = 36;
    private static final int CROP_Y = 31;
    private static final int CROP_W = 41;
    private static final int CROP_H = 29;

    private static final int RENDER_WIDTH = 90;
    private static final int RENDER_HEIGHT = 64;

    public Warrior(double x, double y, InputHandler input) {

        super(
                x,
                y,
                RENDER_WIDTH,
                RENDER_HEIGHT,
                new Stats(150, 50, 30, 25, 4.0), input,
                Constants.PLAYER_SPRITE
        );

        addSkill(new Skill("Golpe Pesado", 40, 10
        ));

        BufferedImage sheet = SpriteLoader.load("/sprites/Soldier.png");
        animations.put(AnimState.IDLE, new Animation(
                SpriteSheet.sliceRowCropped(sheet, 0, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 150));
        animations.put(AnimState.WALK, new Animation(
                SpriteSheet.sliceRowCropped(sheet, 1, 8, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 90));
        animations.put(AnimState.ATTACK, new Animation(
                SpriteSheet.sliceRowCropped(sheet, 2, 6, CELL_SIZE, CELL_SIZE, CROP_X, CROP_Y, CROP_W, CROP_H), 70));
    }

    @Override
    protected void handleAttack() {
    }
}

