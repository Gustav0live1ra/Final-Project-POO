package com.rpgwave.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;

public class HeavySlashEffect extends Entity {

    private static final int EFFECT_WIDTH = 70;
    private static final int EFFECT_HEIGHT = 70;

    private static final int FRAME_COUNT = 9;

    private static final long FRAME_DURATION = 80;

    private final BufferedImage[] frames;

    private final double angle;

    private int currentFrame = 0;

    private long lastFrameTime;

    public HeavySlashEffect(
            double x,
            double y,
            double targetX,
            double targetY) {

        super(
                targetX - EFFECT_WIDTH / 2.0,
                targetY - EFFECT_HEIGHT / 2.0,
                EFFECT_WIDTH,
                EFFECT_HEIGHT
        );
        frames = loadGifFrames(
                "/sprites/slash_fire.gif"
                );

        double dx = targetX - x;
        double dy = targetY - y;

        angle = Math.atan2(dy, dx);

        lastFrameTime =
                System.currentTimeMillis();
    }

    private BufferedImage[] loadGifFrames(
            String path) {

        try {

            InputStream input =
                    HeavySlashEffect.class
                            .getResourceAsStream(path);

            if (input == null) {
                throw new RuntimeException(
                        "Sprite não encontrado: " + path
                );
            }

            ImageInputStream imageInput =
                    ImageIO.createImageInputStream(input);

            ImageReader reader =
                    ImageIO.getImageReadersByFormatName("gif")
                            .next();

            reader.setInput(imageInput);

            int frameCount =
                    reader.getNumImages(true);

            BufferedImage[] result =
                    new BufferedImage[frameCount];

            for (int i = 0; i < frameCount; i++) {

                result[i] =
                        reader.read(i);
            }

            reader.dispose();
            imageInput.close();
            input.close();

            return result;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao carregar GIF: " + path,
                    e
            );
        }
    }

    @Override
    public void update(
            int worldWidth,
            int worldHeight) {

        long currentTime =
                System.currentTimeMillis();

        if (currentTime - lastFrameTime
                >= FRAME_DURATION) {

            currentFrame++;

            lastFrameTime = currentTime;

            if (currentFrame >= frames.length) {

                active = false;
            }
        }
    }

    @Override
    public void render(Graphics g) {

        if (!active || frames.length == 0) {
            return;
        }

        BufferedImage frame =
                frames[currentFrame];

        Graphics2D g2 =
                (Graphics2D) g.create();

        try {

            int centerX =
                    (int) position.getX()
                            + width / 2;

            int centerY =
                    (int) position.getY()
                            + height / 2;

            g2.translate(
                    centerX,
                    centerY
            );

            /*
             * O sprite original está
             * inclinado na diagonal.
             *
             * Esse ajuste pode ser alterado
             * depois caso a direção fique
             * invertida.
             */
            g2.rotate(
                    angle
            );

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