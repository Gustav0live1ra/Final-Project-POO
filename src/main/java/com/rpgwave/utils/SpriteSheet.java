package com.rpgwave.utils;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    public static BufferedImage[] sliceRow(BufferedImage sheet, int row, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = sheet.getSubimage(i * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
        return frames;
    }
}