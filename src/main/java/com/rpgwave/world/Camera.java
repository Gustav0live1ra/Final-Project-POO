package com.rpgwave.world;

import com.rpgwave.entities.Entity;

public class Camera {
    private double x, y;
    private final int viewWidth, viewHeight;
    private final int worldPixelWidth, worldPixelHeight;

    public Camera(int viewWidth, int viewHeight, int worldPixelWidth, int worldPixelHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.worldPixelWidth = worldPixelWidth;
        this.worldPixelHeight = worldPixelHeight;
    }

    public void follow(Entity target) {
        x = target.getCenterX() - viewWidth / 2.0;
        y = target.getCenterY() - viewHeight / 2.0;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + viewWidth > worldPixelWidth) x = worldPixelWidth - viewWidth;
        if (y + viewHeight > worldPixelHeight) y = worldPixelHeight - viewHeight;
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
}