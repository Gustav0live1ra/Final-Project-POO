package com.rpgwave.entities;

import java.awt.Color;
import java.awt.Graphics;

public class AttackEffect {

    private double x;
    private double y;

    private int width;
    private int height;

    private int duration;
    private int currentTime;

    private final CharacterType characterType;

    public AttackEffect(
            double x,
            double y,
            CharacterType characterType) {

        this.x = x;
        this.y = y;

        this.characterType = characterType;

        this.currentTime = 0;
        this.duration = 150;

        this.width = 40;
        this.height = 40;
    }

    public void update() {
        currentTime++;
    }

    public boolean isFinished() {
        return currentTime >= duration / 16;
    }

    public void render(Graphics g) {

        switch (characterType) {

            case WARRIOR:
                renderWarriorEffect(g);
                break;

            case ARCHER:
                renderArcherEffect(g);
                break;

            case MAGE:
                renderMageEffect(g);
                break;
        }
    }

    private void renderWarriorEffect(Graphics g) {

        g.setColor(Color.WHITE);

        g.fillRect(
                (int) x,
                (int) y,
                width,
                height
        );
    }

    private void renderArcherEffect(Graphics g) {

        g.setColor(Color.WHITE);

        g.fillRect(
                (int) x,
                (int) y,
                width,
                8
        );
    }

    private void renderMageEffect(Graphics g) {

        g.setColor(Color.ORANGE);

        g.fillOval(
                (int) x,
                (int) y,
                width,
                height
        );
    }
}