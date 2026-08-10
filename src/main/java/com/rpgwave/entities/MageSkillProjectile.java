package com.rpgwave.entities;

import com.rpgwave.utils.Constants;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MageSkillProjectile extends Entity {

    private final double velocityX;
    private final double velocityY;

    private final List<BufferedImage> frames;

    private final double angle;

    // Animação
    private int currentFrame = 0;
    private long lastFrameTime;

    private static final long FRAME_DURATION = 70;

    // Tamanho visual da bola de fogo
    private static final int RENDER_WIDTH = 48;
    private static final int RENDER_HEIGHT = 48;

    public MageSkillProjectile(
            double startX,
            double startY,
            double targetX,
            double targetY) {

        super(
                startX - Constants.PROJECTILE_WIDTH / 2.0,
                startY - Constants.PROJECTILE_HEIGHT / 2.0,
                Constants.PROJECTILE_WIDTH,
                Constants.PROJECTILE_HEIGHT
        );

        // =========================================================
        // CALCULA A DIREÇÃO DO PROJÉTIL
        // =========================================================

        double dx = targetX - startX;
        double dy = targetY - startY;

        double distance = Math.sqrt(
                dx * dx + dy * dy
        );

        double speed = 8;

        if (distance == 0) {

            this.velocityX = 0;
            this.velocityY = 0;

        } else {

            this.velocityX =
                    (dx / distance) * speed;

            this.velocityY =
                    (dy / distance) * speed;
        }

        this.angle = Math.atan2(
                this.velocityY,
                this.velocityX
        );

        // =========================================================
        // CARREGA OS FRAMES DO GIF
        // =========================================================

        this.frames = loadGifFrames(
                "/sprites/fire-bomb.gif"
        );

        this.lastFrameTime =
                System.currentTimeMillis();
    }

    private List<BufferedImage> loadGifFrames(
            String path) {

        List<BufferedImage> loadedFrames =
                new ArrayList<>();

        try {

            InputStream inputStream =
                    MageSkillProjectile.class
                            .getResourceAsStream(path);

            if (inputStream == null) {

                throw new RuntimeException(
                        "Sprite não encontrado: " + path
                );
            }

            ImageInputStream imageInputStream =
                    ImageIO.createImageInputStream(
                            inputStream
                    );

            if (imageInputStream == null) {

                throw new RuntimeException(
                        "Não foi possível abrir o GIF: "
                                + path
                );
            }

            Iterator<ImageReader> readers =
                    ImageIO.getImageReadersByFormatName(
                            "gif"
                    );

            if (!readers.hasNext()) {

                throw new RuntimeException(
                        "Nenhum leitor de GIF disponível."
                );
            }

            ImageReader reader =
                    readers.next();

            reader.setInput(
                    imageInputStream,
                    false,
                    false
            );

            int frameCount =
                    reader.getNumImages(true);

            for (int i = 0; i < frameCount; i++) {

                BufferedImage frame =
                        reader.read(i);

                if (frame != null) {
                    loadedFrames.add(frame);
                }
            }

            reader.dispose();
            imageInputStream.close();
            inputStream.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao carregar GIF: " + path,
                    e
            );
        }

        if (loadedFrames.isEmpty()) {

            throw new RuntimeException(
                    "O GIF não possui frames: " + path
            );
        }

        System.out.println(
                "Fire-bomb carregada: "
                        + loadedFrames.size()
                        + " frames"
        );

        return loadedFrames;
    }

    @Override
    public void update(
            int worldWidth,
            int worldHeight) {

        // =========================================================
        // MOVIMENTO
        // =========================================================

        position.setX(
                position.getX() + velocityX
        );

        position.setY(
                position.getY() + velocityY
        );

        // =========================================================
        // ANIMAÇÃO
        // =========================================================

        long currentTime =
                System.currentTimeMillis();

        if (currentTime - lastFrameTime
                >= FRAME_DURATION) {

            currentFrame++;

            lastFrameTime = currentTime;

            if (currentFrame >= frames.size()) {

                currentFrame = 0;
            }
        }

        // =========================================================
        // SAIU DO MAPA
        // =========================================================

        if (position.getX() + width < 0
                || position.getX() > worldWidth
                || position.getY() + height < 0
                || position.getY() > worldHeight) {

            active = false;
        }
    }

    @Override
    public void render(Graphics g) {

        if (!active || frames.isEmpty()) {
            return;
        }

        BufferedImage frame =
                frames.get(currentFrame);

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
             * A bola de fogo é praticamente circular,
             * então a rotação não é necessária.
             */
            g2.rotate(angle);

            g2.drawImage(
                    frame,
                    -RENDER_WIDTH / 2,
                    -RENDER_HEIGHT / 2,
                    RENDER_WIDTH,
                    RENDER_HEIGHT,
                    null
            );

        } finally {

            g2.dispose();
        }
    }
}