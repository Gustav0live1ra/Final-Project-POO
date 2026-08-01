package com.rpgwave.core;

import java.awt.*;

public class MenuScene implements GameScene {
    private final InputHandler input;
    private final SceneManager sceneManager;
    private final int viewWidth;
    private final int viewHeight;

    public MenuScene(InputHandler input, SceneManager sceneManager, int viewWidth, int viewHeight){
        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

    }
    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void update() {

        if (input.consumeEnter()) {
            sceneManager.switchTo(GameState.CHARACTER_SELECT);
        }

    }

    @Override
    public void render(Graphics g) {

        // Fundo preto
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, viewWidth, viewHeight);

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 40));

        FontMetrics fm = g.getFontMetrics();

        String titulo = "RPG WAVE";

        int xTitulo = (viewWidth - fm.stringWidth(titulo)) / 2;

        g.drawString(
                titulo,
                xTitulo,
                viewHeight / 2
        );


        g.setFont(new Font("Arial", Font.PLAIN, 22));

        fm = g.getFontMetrics();

        String mensagem = "Pressione ENTER para jogar";

        int xMensagem = (viewWidth - fm.stringWidth(mensagem)) / 2;


        g.drawString(
                mensagem,
                xMensagem,
                viewHeight / 2 + 50
        );
    }
}
