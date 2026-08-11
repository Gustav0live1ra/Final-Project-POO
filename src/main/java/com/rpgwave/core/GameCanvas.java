package com.rpgwave.core;

import com.rpgwave.utils.Constants;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas {

    public GameCanvas() {
        Dimension size = new Dimension(
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT
        );
        setPreferredSize(size);
        setFocusable(true);
    }

    public void render(SceneManager sceneManager) {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        try {

            g.clearRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            sceneManager.render(g);

        } finally {
            g.dispose();
        }

        bs.show();
    }
}