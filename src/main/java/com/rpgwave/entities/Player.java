package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    private final InputHandler input;
    private final int speed;
    private final BufferedImage sprite;

    public Player(double x, double y, InputHandler input) {
        super(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        this.input = input;
        this.speed = Constants.PLAYER_SPEED;
        this.sprite = SpriteLoader.load(Constants.PLAYER_SPRITE);
    }


    // Limitar às bordas da tela
    private void clampToBounds(int worldWidth, int worldHeight) {
        if (position.getX() < 0) position.setX(0);
        if (position.getY() < 0) position.setY(0);
        if (position.getX() + width > worldWidth)
            position.setX(worldWidth - width);
        if (position.getY() + height > worldHeight)
            position.setY(worldHeight - height);
    }


    @Override
    public void update(int worldWidth, int worldHeight) {
        // Movimento
        if (input.isUp())    position.setY(position.getY() - speed);
        if (input.isDown())  position.setY(position.getY() + speed);
        if (input.isLeft())  position.setX(position.getX() - speed);
        if (input.isRight()) position.setX(position.getX() + speed);

        clampToBounds(worldWidth, worldHeight);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite,
                (int) position.getX(), (int) position.getY(),
                width, height, null);

        if (Constants.DEBUG_MODE) {
            g.setColor(Color.YELLOW);
            g.drawRect((int) position.getX(), (int) position.getY(),
                    width, height);
        }
    }
}