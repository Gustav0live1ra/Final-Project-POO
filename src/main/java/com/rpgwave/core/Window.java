package com.rpgwave.core;

import com.rpgwave.utils.Constants;
import javax.swing.JFrame;

public class Window extends JFrame {

    public Window(GameCanvas canvas) {
        setTitle(Constants.GAME_TITLE);
        add(canvas);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}