package com.rpgwave.core;

import java.awt.Graphics;

public interface GameScene {
    void onEnter();
    void onExit();
    void update();
    void render(Graphics g);
}