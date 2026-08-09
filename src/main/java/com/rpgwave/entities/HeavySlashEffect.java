package com.rpgwave.entities;

import com.rpgwave.utils.SpriteLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HeavySlashEffect extends Entity {

    private final BufferedImage spriteSheet;

    private static final int FRAME_WIDTH = 126;
    private static final int FRAME_HEIGHT = 356;

    private static final int EFFECT_WIDTH = 100;
    private static final int EFFECT_HEIGHT = 100;

    private final double angle;

    public HeavySlashEffect(
            double x,
            double y,
            double targetX,
            double targetY) {

        super(
                x - EFFECT_WIDTH / 2.0,
                y - EFFECT_HEIGHT / 2.0,
                EFFECT_WIDTH,
                EFFECT_HEIGHT
        );

        spriteSheet =
                SpriteLoader.load("/sprites/HeavySlash.png");

        double dx = targetX - x;
        double dy = targetY - y;

        angle = Math.atan2(dy, dx);
    }

    @Override
    public void update(
            int worldWidth,
            int worldHeight) {

        // Apenas teste visual por enquanto.
        // O efeito fica ativo.
    }

    @Override
    public void render(Graphics g) {

        if (spriteSheet == null) {
            return;
        }

        // Primeiro frame
        BufferedImage frame =
                spriteSheet.getSubimage(
                        0,
                        69,
                        126,
                        195
                );

        Graphics2D g2 =
                (Graphics2D) g.create();

        try {

            int centerX =
                    (int) position.getX() + width / 2;

            int centerY =
                    (int) position.getY() + height / 2;

            g2.translate(centerX, centerY);

            g2.rotate(angle);

            g2.drawImage(
                    frame,
                    -width / 2,
                    -height / 2,
                    width,
                    height,
                    null
            );

        } finally {
            g2.dispose();
        }
    }
}