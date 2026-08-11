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
            // Calcula quanto tempo passou desde a última volta do while
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            // Se o delta for maior ou igual a 1, significa que já passou 1/60 avos de segundo
            if (delta >= 1) {
                game.update();
                game.render();
                frames++;
                delta--; // Tira 1 do delta (mantém a sobra de tempo para a próxima precisão)
            }

            // Exibe o FPS a cada 1 segundo (1000 milissegundos)
            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer = System.currentTimeMillis();
            }


            // Da um pequeno sleep de 1 ou 2 ms se a máquina for muito rápida,
            // pro jogo não consumir 100% do núcleo do processador atoa
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}