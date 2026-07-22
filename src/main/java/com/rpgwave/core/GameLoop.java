package com.rpgwave.core;

import com.rpgwave.utils.Constants;

public class GameLoop implements Runnable {

    private final Game game;
    private final Thread thread;
    private boolean running;

    public GameLoop(Game game) {
        this.game = game;
        this.thread = new Thread(this, "GameLoop");
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long previousTime = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            try {
                Thread.sleep(Constants.FRAME_TIME_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            game.update();
            game.render();
            frames++;

            long currentTime = System.currentTimeMillis();
            if (currentTime - previousTime >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                previousTime = currentTime;
            }
        }
    }
}