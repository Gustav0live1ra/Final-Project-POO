package com.rpgwave.entities;

import com.rpgwave.utils.SpriteLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SwordSlashEffect extends Entity {

    private final BufferedImage spriteSheet;

    private int currentFrame;
    private long lastFrameTime;

    private static final int FRAME_WIDTH = 64;
    private static final int FRAME_HEIGHT = 47;

    private static final int COLUMNS = 3;
    private static final int ROWS = 3;

    private static final long FRAME_DURATION = 80;

    public SwordSlashEffect(
            double x,
            double y) {

        super(
                x - FRAME_WIDTH / 2.0,
                y - FRAME_HEIGHT / 2.0,
                FRAME_WIDTH,
                FRAME_HEIGHT
        );

        this.spriteSheet =
                SpriteLoader.load("/sprites/SwordSlash.png");

        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    @Override
    public void update(
            int worldWidth,
            int worldHeight) {

        long currentTime =
                System.currentTimeMillis();

        if (currentTime - lastFrameTime >= FRAME_DURATION) {

            currentFrame++;

            lastFrameTime = currentTime;

            if (currentFrame >= COLUMNS * ROWS) {
                active = false;
            }
        }
    }

    @Override
    public void render(Graphics g) {

        if (spriteSheet == null || !active) {
            return;
        }

        int column =
                currentFrame % COLUMNS;

        int row =
                currentFrame / COLUMNS;

        BufferedImage frame =
                spriteSheet.getSubimage(
                        column * FRAME_WIDTH,
                        row * FRAME_HEIGHT,
                        FRAME_WIDTH,
                        FRAME_HEIGHT
                );

        g.drawImage(
                frame,
                (int) position.getX(),
                (int) position.getY(),
                width,
                height,
                null
        );
    }
}