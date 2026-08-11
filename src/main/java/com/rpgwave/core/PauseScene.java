package com.rpgwave.core;

import java.awt.*;

public class PauseScene implements GameScene {

    private final InputHandler input;
    private final SceneManager sceneManager;
    private final int viewWidth;
    private final int viewHeight;

    private float pausePulse = 0f;

    public PauseScene(
            InputHandler input,
            SceneManager sceneManager,
            int viewWidth,
            int viewHeight) {

        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    @Override
    public void onEnter() {
        pausePulse = 0f;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void update() {
        pausePulse += 0.05f;

        if (input.consumeEnter()){
            sceneManager.switchTo(GameState.PLAYING);
            return;
        }
        if (input.consumeM()){
            sceneManager.switchTo(GameState.MENU);
        }
    }

    @Override
    public void render(Graphics g) {
        drawBackground(g);
        drawPauseOverlay(g);
        drawTitle(g);
        drawPauseOptions(g);
        drawControlsHint(g);
        drawFooter(g);
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(new Color(18, 18, 18));
        g.fillRect(0, 0, viewWidth, viewHeight);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 20, 30, 100),
                0, viewHeight, new Color(18, 18, 18, 200)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, viewWidth, viewHeight);

        g.setColor(new Color(255, 215, 0, 8));
        g.fillOval(-150, -150, 500, 500);
        g.fillOval(viewWidth - 350, viewHeight - 350, 600, 600);
    }

    private void drawPauseOverlay(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(centerX - 250, centerY - 180, 500, 360, 30, 30);

        g2d.setColor(new Color(255, 215, 0, 80));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(centerX - 250, centerY - 180, 500, 360, 30, 30);

        g.setColor(new Color(255, 215, 0, 20));
        g.fillRoundRect(centerX - 240, centerY - 170, 480, 340, 25, 25);
    }

    private void drawTitle(Graphics g) {
        String titulo = "JOGO PAUSADO";

        float pulse = 0.7f + 0.3f * (float) Math.sin(pausePulse * 2);
        int alpha = (int)(200 * pulse);

        g.setFont(new Font("Serif", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(titulo)) / 2;
        int y = viewHeight / 2 - 100;

        g.setColor(Color.DARK_GRAY);
        g.drawString(titulo, x + 3, y + 3);

        g.setColor(new Color(255, 215, 0, alpha));
        g.drawString(titulo, x, y);

        g.setColor(new Color(255, 215, 0, 60));
        g.fillRect(viewWidth / 2 - 150, y + 15, 300, 2);

        String subtitulo = "⏸ GAME PAUSED";
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        x = (viewWidth - fm.stringWidth(subtitulo)) / 2;
        g.setColor(Color.GRAY);
        g.drawString(subtitulo, x, y + 45);
    }

    private void drawPauseOptions(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;

        int panelX = centerX - 250;
        int panelY = centerY - 180;
        int panelHeight = 360;

        int btnContinuarWidth = 260;
        int btnContinuarHeight = 50;
        int btnContinuarX = centerX - (btnContinuarWidth / 2);
        int btnContinuarY = panelY + 130;

        g2d.setColor(new Color(0, 80, 20, 200));
        g2d.fillRoundRect(btnContinuarX, btnContinuarY, btnContinuarWidth, btnContinuarHeight, 10, 10);
        g2d.setColor(new Color(50, 255, 50, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnContinuarX, btnContinuarY, btnContinuarWidth, btnContinuarHeight, 10, 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.GREEN);
        String continuar = "▶ CONTINUAR";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(continuar, centerX - (fm.stringWidth(continuar) / 2), btnContinuarY + 35);

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
        fm = g2d.getFontMetrics();
        int menuTextoX = btnMenuX + (btnMenuWidth - fm.stringWidth(menuTexto)) / 2;
        int menuTextoY = btnMenuY + 28;
        g2d.drawString(menuTexto, menuTextoX, menuTextoY);
    }
    private void drawControlsHint(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void drawFooter(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.GRAY);

        String texto = "Versão 1.0";
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(texto)) / 2;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x - 20, viewHeight - 40, fm.stringWidth(texto) + 40, 28, 10, 10);

        g.setColor(new Color(150, 150, 150));
        g.drawString(texto, x, viewHeight - 22);
    }
}