package com.rpgwave.core;

import java.awt.*;

public class PauseScene implements GameScene {

    private final InputHandler input;
    private final SceneManager sceneManager;
    private final int viewWidth;
    private final int viewHeight;

    // Variável para animação
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

    // ============ MÉTODOS DE DESENHO ============

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Fundo escuro (igual ao menu)
        g.setColor(new Color(18, 18, 18));
        g.fillRect(0, 0, viewWidth, viewHeight);

        // Gradiente decorativo
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 20, 30, 100),
                0, viewHeight, new Color(18, 18, 18, 200)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, viewWidth, viewHeight);

        // Círculos decorativos de fundo
        g.setColor(new Color(255, 215, 0, 8));
        g.fillOval(-150, -150, 500, 500);
        g.fillOval(viewWidth - 350, viewHeight - 350, 600, 600);
    }

    private void drawPauseOverlay(Graphics g) {
        // Efeito de vidro/overlay no centro
        Graphics2D g2d = (Graphics2D) g;

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;

        // Círculo central com transparência
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(centerX - 250, centerY - 180, 500, 360, 30, 30);

        // Borda do painel (dourada)
        g2d.setColor(new Color(255, 215, 0, 80));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(centerX - 250, centerY - 180, 500, 360, 30, 30);

        // Efeito de brilho interno
        g.setColor(new Color(255, 215, 0, 20));
        g.fillRoundRect(centerX - 240, centerY - 170, 480, 340, 25, 25);
    }

    private void drawTitle(Graphics g) {
        String titulo = "JOGO PAUSADO";

        // Ícone de pause animado
        float pulse = 0.7f + 0.3f * (float) Math.sin(pausePulse * 2);
        int alpha = (int)(200 * pulse);

        g.setFont(new Font("Serif", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(titulo)) / 2;
        int y = viewHeight / 2 - 100;

        // Sombra do título (como no menu)
        g.setColor(Color.DARK_GRAY);
        g.drawString(titulo, x + 3, y + 3);

        // Título principal (dourado pulsante)
        g.setColor(new Color(255, 215, 0, alpha));
        g.drawString(titulo, x, y);

        // Linha decorativa abaixo do título
        g.setColor(new Color(255, 215, 0, 60));
        g.fillRect(viewWidth / 2 - 150, y + 15, 300, 2);

        // Subtítulo
        String subtitulo = "⏸ GAME PAUSED";
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        x = (viewWidth - fm.stringWidth(subtitulo)) / 2;
        g.setColor(Color.GRAY);
        g.drawString(subtitulo, x, y + 45);
    }

    private void drawPauseOptions(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Ativar anti-aliasing para as fontes e bordas ficarem bonitas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;

        // 1. DESENHAR O PAINEL DE FUNDO (A "MINI TELA")
        int panelWidth = 500;
        int panelHeight = 400;
        int panelX = centerX - (panelWidth / 2);
        int panelY = centerY - (panelHeight / 2);

        // Sombra do painel
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(panelX + 5, panelY + 5, panelWidth, panelHeight, 20, 20);

        // Fundo escuro do painel
        g2d.setColor(new Color(30, 25, 10, 230)); // Cor de fundo marrom escura
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        // Borda dourada do painel
        g2d.setColor(new Color(255, 215, 0, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);


        // 2. TÍTULO "JOGO PAUSADO" E SUBTÍTULO
        g2d.setFont(new Font("Serif", Font.BOLD, 50));
        String titulo = "JOGO PAUSADO";
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = centerX - (fm.stringWidth(titulo) / 2);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(titulo, tituloX + 2, panelY + 65);
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(titulo, tituloX, panelY + 63);

        // Traço dourado
        g2d.setColor(new Color(255, 215, 0, 100));
        g2d.fillRect(centerX - 80, panelY + 80, 160, 2);

        // "II GAME PAUSED"
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.LIGHT_GRAY);
        String sub = "II GAME PAUSED";
        fm = g2d.getFontMetrics();
        g2d.drawString(sub, centerX - (fm.stringWidth(sub) / 2), panelY + 105);


        // 3. BOTÃO "CONTINUAR" (Centralizado)
        int btnContinuarWidth = 260;
        int btnContinuarHeight = 50;
        int btnContinuarX = centerX - (btnContinuarWidth / 2);
        int btnContinuarY = panelY + 130; // Posicionado logo abaixo do subtítulo

        // Fundo verde-escuro do botão
        g2d.setColor(new Color(0, 80, 20, 200));
        g2d.fillRoundRect(btnContinuarX, btnContinuarY, btnContinuarWidth, btnContinuarHeight, 10, 10);
        // Borda verde clara
        g2d.setColor(new Color(50, 255, 50, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnContinuarX, btnContinuarY, btnContinuarWidth, btnContinuarHeight, 10, 10);

        // Texto do botão
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.GREEN);
        String continuar = "▶ CONTINUAR";
        fm = g2d.getFontMetrics();
        g2d.drawString(continuar, centerX - (fm.stringWidth(continuar) / 2), btnContinuarY + 35);


        // 4. BOTÃO "MENU PRINCIPAL" (Canto Esquerdo do painel, com a tecla [M])
        int btnMenuWidth = 180;
        int btnMenuHeight = 40;
        int btnMenuX = panelX + 20; // Encostado na borda esquerda do painel
        int btnMenuY = panelY + panelHeight - 60; // Lá embaixo

        // Fundo marrom do botão
        g2d.setColor(new Color(100, 70, 30, 200));
        g2d.fillRoundRect(btnMenuX, btnMenuY, btnMenuWidth, btnMenuHeight, 8, 8);
        // Borda amarela
        g2d.setColor(new Color(200, 170, 100, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnMenuX, btnMenuY, btnMenuWidth, btnMenuHeight, 8, 8);

        // Texto do botão com a tecla
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(new Color(255, 220, 150));
        String menuTexto = "[M] Menu Principal";
        fm = g2d.getFontMetrics();
        // Centraliza o texto dentro do botão marrom
        int menuTextoX = btnMenuX + (btnMenuWidth - fm.stringWidth(menuTexto)) / 2;
        int menuTextoY = btnMenuY + 28;
        g2d.drawString(menuTexto, menuTextoX, menuTextoY);
    }
    private void drawControlsHint(Graphics g) {
        // Dicas de controle estilizadas
        g.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void drawFooter(Graphics g) {
        // Rodapé com versão do jogo (igual ao menu)
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.GRAY);

        String texto = "Versão 1.0";
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(texto)) / 2;

        // Fundo do footer
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x - 20, viewHeight - 40, fm.stringWidth(texto) + 40, 28, 10, 10);

        g.setColor(new Color(150, 150, 150));
        g.drawString(texto, x, viewHeight - 22);
    }
}