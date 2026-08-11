package com.rpgwave.entities;

import com.rpgwave.utils.Vector2D;
import java.awt.Graphics;

public abstract class Entity {

    protected Vector2D position;
    protected int width;
    protected int height;
    protected boolean active;

    public Entity(double x, double y, int width, int height) {
        this.position = new Vector2D(x, y);
        this.width = width;
        this.height = height;
        this.active = true;
    }

    public abstract void update(int worldWidth, int worldHeight);
    public abstract void render(Graphics g);

    public boolean collidesWith(Entity other) {
        return position.getX() < other.position.getX() + other.width &&
                position.getX() + width > other.position.getX() &&
                position.getY() < other.position.getY() + other.height &&
                position.getY() + height > other.position.getY();
    }

    public Vector2D getPosition() { return position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public double getCenterX() { return position.getX() + width / 2.0; }
    public double getCenterY() { return position.getY() + height / 2.0; }
}