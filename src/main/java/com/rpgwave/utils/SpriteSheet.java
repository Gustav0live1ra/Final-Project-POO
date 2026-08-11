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

    public static BufferedImage[] sliceRowCropped(BufferedImage sheet, int row, int frameCount,
            int cellWidth, int cellHeight, int cropX, int cropY, int cropWidth, int cropHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            int baseX = i * cellWidth;
            int baseY = row * cellHeight;
            frames[i] = sheet.getSubimage(baseX + cropX, baseY + cropY, cropWidth, cropHeight);
        }
        return frames;
    }
}