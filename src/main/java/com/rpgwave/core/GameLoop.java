package com.rpgwave.core;

import static com.rpgwave.utils.Constants.TARGET_FPS;

public class GameLoop implements Runnable {

    private final Game game;
    private final Thread thread;
    private boolean running;

    // 1 Segundo = 1 Bilhão de Nanosegundos. Dividimos por 60 fps.
    private final double TIME_PER_TICK = 1000000000.0 / TARGET_FPS;

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

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            if (delta >= 1) {
                game.update();
                game.render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer = System.currentTimeMillis();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}