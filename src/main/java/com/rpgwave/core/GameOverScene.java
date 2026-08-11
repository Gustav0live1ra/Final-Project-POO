package com.rpgwave.core;

import com.rpgwave.entities.CharacterType;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverScene implements GameScene {
    private final SceneManager sceneManager;
    private final InputHandler input;
    private final int viewWidth;
    private final int viewHeight;

    private float pulseTime = 0f;

    public GameOverScene(InputHandler input, SceneManager sceneManager, int viewWidth, int viewHeight) {
        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    @Override
    public void onEnter() {
        pulseTime = 0f;
    }

    @Override
    public void onExit() {}

    @Override
    public void update() {
        pulseTime += 0.05f;

        if (input.consumeEnter()) {
            sceneManager.addScene(GameState.PLAYING,
                    new PlayingScene(input, sceneManager, viewWidth, viewHeight, Game.currentCharacter));
            sceneManager.switchTo(GameState.PLAYING);
        }

        if (input.consumeM()) {
            sceneManager.switchTo(GameState.MENU);
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, viewWidth, viewHeight);

        int panelWidth = 500;
        int panelHeight = 400;
        int panelX = centerX - (panelWidth / 2);
        int panelY = centerY - (panelHeight / 2);

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(panelX + 5, panelY + 5, panelWidth, panelHeight, 20, 20);
        g2d.setColor(new Color(30, 5, 5, 230));
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
        g2d.setColor(new Color(200, 0, 0, 180));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        g2d.setFont(new Font("Serif", Font.BOLD, 55));
        String titulo = "GAME OVER";
        FontMetrics fmTitulo = g2d.getFontMetrics();
        int tituloX = centerX - (fmTitulo.stringWidth(titulo) / 2);
        g2d.setColor(Color.BLACK);
        g2d.drawString(titulo, tituloX + 3, panelY + 65);
        g2d.setColor(new Color(200, 0, 0));
        g2d.drawString(titulo, tituloX, panelY + 63);
        g2d.setColor(new Color(200, 0, 0, 100));
        g2d.fillRect(centerX - 80, panelY + 80, 160, 2);

        float pulseAlpha = 0.6f + 0.4f * (float) Math.sin(pulseTime * 2);
        int alpha = (int) (150 * pulseAlpha);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(new Color(255, 100, 100, alpha));
        String sub = "VOCÊ FOI DERROTADO...";
        FontMetrics fmSub = g2d.getFontMetrics();
        g2d.drawString(sub, centerX - (fmSub.stringWidth(sub) / 2), panelY + 105);

        int btnRestartWidth = 340;
        int btnRestartHeight = 55;
        int btnRestartX = centerX - (btnRestartWidth / 2);
        int btnRestartY = panelY + 140;

        g2d.setColor(new Color(120, 0, 0, 200));
        g2d.fillRoundRect(btnRestartX, btnRestartY, btnRestartWidth, btnRestartHeight, 10, 10);
        g2d.setColor(new Color(255, 50, 50, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnRestartX, btnRestartY, btnRestartWidth, btnRestartHeight, 10, 10);

        g2d.setColor(new Color(255, 220, 220));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        int centerYButton = btnRestartY + (btnRestartHeight / 2);
        FontMetrics fmRestart = g2d.getFontMetrics();

        String icon = "↻";
        int iconX = btnRestartX + 30;
        int iconY = centerYButton + (fmRestart.getAscent() / 2) - 2;
        g2d.drawString(icon, iconX, iconY);

        String restart = "JOGAR NOVAMENTE";
        int textoX = iconX + fmRestart.stringWidth(icon) + 15;
        int textoY = centerYButton + (fmRestart.getAscent() / 2) - 2;
        g2d.drawString(restart, textoX, textoY);

        int btnMenuWidth = 180;
        int btnMenuHeight = 40;
        int btnMenuX = panelX + 20;
        int btnMenuY = panelY + panelHeight - 60;

        g2d.setColor(new Color(100, 70, 30, 200));
        g2d.fillRoundRect(btnMenuX, btnMenuY, btnMenuWidth, btnMenuHeight, 8, 8);
        g2d.setColor(new Color(200, 170, 100, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnMenuX, btnMenuY, btnMenuWidth, btnMenuHeight, 8, 8);

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(new Color(255, 220, 150));
        String menuTexto = "[M] Menu Principal";

        FontMetrics fmMenu = g2d.getFontMetrics();
        int menuTextoX = btnMenuX + (btnMenuWidth - fmMenu.stringWidth(menuTexto)) / 2;
        int menuTextoY = btnMenuY + ((btnMenuHeight - fmMenu.getHeight()) / 2) + fmMenu.getAscent();

        g2d.drawString(menuTexto, menuTextoX, menuTextoY);
    }
}