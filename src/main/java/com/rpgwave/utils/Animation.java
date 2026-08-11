package com.rpgwave.utils;

import java.awt.image.BufferedImage;

public class Animation {

    private final BufferedImage[] frames;
    private final long frameDurationMs;
    private long lastFrameTime;
    private int currentFrame = 0;

    public Animation(BufferedImage[] frames, long frameDurationMs) {
        this.frames = frames;
        this.frameDurationMs = frameDurationMs;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public BufferedImage getCurrentFrame() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime >= frameDurationMs) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastFrameTime = now;
        }
        return frames[currentFrame];
    }
}