package com.rpgwave.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SpriteLoader {

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(
                    Objects.requireNonNull(
                            SpriteLoader.class.getResourceAsStream(path),
                            "Sprite não encontrado: " + path
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar sprite: " + path, e);
        }
    }
}