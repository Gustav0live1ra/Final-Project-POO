package com.rpgwave.core;

import com.rpgwave.entities.CharacterType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CharacterSelectScene implements GameScene {
    private final SceneManager sceneManager;
    private CharacterType selectedCharacter;

    private final InputHandler input;
    private final int viewWidth;
    private final int viewHeight;

    // Variáveis para animação
    private float selectionPulse = 0f;

    // ========== IMAGENS DOS PERSONAGENS ==========
    private BufferedImage warriorImage;
    private BufferedImage archerImage;
    private BufferedImage mageImage;
    private boolean imagesLoaded = false;

    public CharacterSelectScene(
            InputHandler input, SceneManager sceneManager,
            int viewWidth,
            int viewHeight
    ){
        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        selectedCharacter = CharacterType.ARCHER;

        loadImages();
    }

    // ============ METODO PARA CARREGAR IMAGENS ============
    private void loadImages() {
        try {
            warriorImage = ImageIO.read(getClass().getResourceAsStream("/sprites/Guerreiro.png"));
            archerImage = ImageIO.read(getClass().getResourceAsStream("/sprites/Arqueiro.png"));
            mageImage = ImageIO.read(getClass().getResourceAsStream("/sprites/Mago.png"));

            if (warriorImage != null && archerImage != null && mageImage != null) {
                imagesLoaded = true;
                System.out.println("✅ Imagens carregadas com sucesso!");
            } else {
                System.out.println("⚠️ Alguma imagem não foi encontrada!");
                createFallbackImages();
            }
        } catch (IOException e) {
            System.out.println("❌ Erro ao carregar imagens: " + e.getMessage());
            createFallbackImages();
        }
    }

    // Imagens de fallback
    private void createFallbackImages() {
        warriorImage = createPlaceholderImage(Color.RED, "G");
        archerImage = createPlaceholderImage(Color.GREEN, "A");
        mageImage = createPlaceholderImage(Color.BLUE, "M");
        imagesLoaded = false;
    }

    private BufferedImage createPlaceholderImage(Color color, String letter) {
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Fundo redondo
        g2d.setColor(color);
        g2d.fillOval(10, 10, 180, 180);

        // Letra
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (200 - fm.stringWidth(letter)) / 2;
        int y = (200 - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(letter, x, y);

        g2d.dispose();
        return img;
    }

    // ============ METODO COM ZOOM INDIVIDUAL POR PERSONAGEM ============
    private BufferedImage cropToCircle(BufferedImage srcImage, int diameter, CharacterType type) {
        BufferedImage destImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = destImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
        g2d.setClip(circle);

        int srcWidth = srcImage.getWidth();
        int srcHeight = srcImage.getHeight();

        // --- ZOOM PERSONALIZADO PARA CADA CLASSE ---
        float zoomMultiplier = 1.2f; // Valor padrão (o que ficou bom pro Arqueiro)

        switch (type) {
            case ARCHER:
                zoomMultiplier = 1.1f; // O Arqueiro já está ótimo assim
                break;
            case WARRIOR:
                zoomMultiplier = 0.9f; // O Guerreiro pode chegar mais perto (já que a espada é pra baixo)
                break;
            case MAGE:
                zoomMultiplier = 0.88f; // O Mago tem o cajado e mãos mais fechadas no corpo, pode chegar bem perto!
                break;
        }

        double scale = Math.max(
                (double) diameter / srcWidth,
                (double) diameter / srcHeight
        ) * zoomMultiplier;

        int scaledWidth = (int) (srcWidth * scale);
        int scaledHeight = (int) (srcHeight * scale);

        int x = (diameter - scaledWidth) / 2;
        int y = (diameter - scaledHeight) / 2;

        g2d.drawImage(srcImage, x, y, scaledWidth, scaledHeight, null);

        g2d.setClip(null);
        g2d.setColor(new Color(255, 215, 0, 80));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(1, 1, diameter - 2, diameter - 2);

        g2d.dispose();
        return destImage;
    }

    @Override
    public void onEnter() {
        selectionPulse = 0f;
    }

    @Override
    public void onExit() {
        // Cleanup
    }

    @Override
    public void update(){
        selectionPulse += 0.05f;

        if(input.consumeKey(KeyEvent.VK_RIGHT)){
            selectedCharacter = CharacterType.values()
                    [(selectedCharacter.ordinal() + 1)
                    % CharacterType.values().length];
            selectionPulse = 0f;
        }

        if(input.consumeKey(KeyEvent.VK_LEFT)){
            selectedCharacter = CharacterType.values()
                    [(selectedCharacter.ordinal() - 1 + CharacterType.values().length)
                    % CharacterType.values().length];
            selectionPulse = 0f;
        }

        if (input.consumeEnter()) {
            Game.currentCharacter = selectedCharacter;
            sceneManager.addScene(
                    GameState.PLAYING,
                    new PlayingScene(
                            input,
                            sceneManager,
                            viewWidth,
                            viewHeight,
                            selectedCharacter
                    )
            );
            sceneManager.switchTo(GameState.PLAYING);
        }
    }

    @Override
    public void render(Graphics g){
        drawBackground(g);
        drawTitle(g);
        drawCharacterDisplay(g);
        drawCharacterInfo(g);
        drawNavigationHints(g);
        drawControlsHint(g);

        if (!imagesLoaded) {
            drawImageWarning(g);
        }
    }

    // ============ METODOS DE DESENHO ============

    private void drawBackground(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(new Color(18, 18, 18));
        g.fillRect(0, 0, viewWidth, viewHeight);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(30, 20, 40, 50),
                0, viewHeight, new Color(18, 18, 18, 200)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, viewWidth, viewHeight);

        g.setColor(new Color(255, 215, 0, 10));
        g.fillOval(-100, -100, 400, 400);
        g.fillOval(viewWidth - 300, viewHeight - 300, 500, 500);
    }

    private void drawTitle(Graphics g){
        String titulo = "ESCOLHA SEU PERSONAGEM";

        g.setFont(new Font("Serif", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(titulo)) / 2;

        g.setColor(Color.DARK_GRAY);
        g.drawString(titulo, x + 3, 83);

        g.setColor(new Color(255, 215, 0));
        g.drawString(titulo, x, 80);

        g.setColor(new Color(255, 215, 0, 80));
        g.fillRect(viewWidth / 2 - 120, 95, 240, 2);
    }

    private void drawCharacterDisplay(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2 - 30;

        int circleRadius = 130; // Aumentei um pouco
        int diameter = circleRadius * 2;

        // Efeito de pulso
        float pulse = 1f + 0.03f * (float) Math.sin(selectionPulse * 2);
        int currentRadius = (int)(circleRadius * pulse);
        int currentDiameter = currentRadius * 2;

        // Glow externo
        int glowAlpha = 30 + 20 * (int)(Math.sin(selectionPulse * 2) * 0.5 + 0.5);
        g.setColor(new Color(255, 215, 0, glowAlpha));
        g.fillOval(
                centerX - currentRadius - 25,
                centerY - currentRadius - 25,
                currentDiameter + 50,
                currentDiameter + 50
        );

        // ========== DESENHA A IMAGEM CIRCULAR ==========
        BufferedImage characterImage = getCharacterImage(selectedCharacter);

        if (characterImage != null) {
            // Corta a imagem em círculo
            BufferedImage circularImage = cropToCircle(characterImage, diameter, selectedCharacter);

            // Desenha a imagem circular
            int imageX = centerX - currentRadius;
            int imageY = centerY - currentRadius;
            g.drawImage(circularImage, imageX, imageY, null);

        } else {
            // Fallback: círculo colorido com inicial
            g.setColor(new Color(40, 40, 50));
            g.fillOval(
                    centerX - currentRadius,
                    centerY - currentRadius,
                    currentDiameter,
                    currentDiameter
            );

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String name = selectedCharacter.getDisplayName().substring(0, 1);
            FontMetrics fm = g.getFontMetrics();
            int x = centerX - fm.stringWidth(name) / 2;
            int y = centerY + fm.getAscent() / 2;
            g.drawString(name, x, y);
        }

        // Borda dourada do círculo (por cima da imagem)
        g2d.setColor(new Color(255, 215, 0, 180));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawOval(
                centerX - currentRadius,
                centerY - currentRadius,
                currentDiameter,
                currentDiameter
        );
    }

    private void drawCharacterInfo(Graphics g){
        String nome = selectedCharacter.getDisplayName();

        g.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(nome)) / 2;
        int y = viewHeight / 2 + 140;

        g.setColor(Color.DARK_GRAY);
        g.drawString(nome, x + 2, y + 2);

        g.setColor(getCharacterColor(selectedCharacter));
        g.drawString(nome, x, y);

        String descricao = selectedCharacter.getDescription();
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        x = (viewWidth - fm.stringWidth(descricao)) / 2;
        y += 40;

        int textWidth = fm.stringWidth(descricao) + 40;
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(
                x - 20,
                y - 22,
                textWidth,
                36,
                10,
                10
        );

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(descricao, x, y + 5);
    }

    private void drawNavigationHints(Graphics g){
        int arrowY = viewHeight / 2;
        int totalChars = CharacterType.values().length;
        int currentIndex = selectedCharacter.ordinal();

        if (currentIndex > 0) {
            drawArrow(g, 30, arrowY, "◀", true);
        }

        if (currentIndex < totalChars - 1) {
            drawArrow(g, viewWidth - 70, arrowY, "▶", true);
        }
    }

    private void drawArrow(Graphics g, int x, int y, String arrow, boolean active){
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();

        int arrowX = x;
        int arrowY = y + fm.getAscent() / 2;

        if (active) {
            float pulse = 0.7f + 0.3f * (float) Math.sin(selectionPulse * 1.5);
            int alpha = (int)(200 * pulse);

            g.setColor(new Color(0, 0, 0, 100));
            g.drawString(arrow, arrowX + 2, arrowY + 2);

            g.setColor(new Color(255, 215, 0, alpha));
            g.drawString(arrow, arrowX, arrowY);
        }
    }

    private void drawControlsHint(Graphics g){
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.GRAY);

        String texto = "← → Navegar    ENTER Confirmar";
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(texto)) / 2;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x - 20, viewHeight - 45, fm.stringWidth(texto) + 40, 30, 10, 10);

        g.setColor(Color.GRAY);
        g.drawString(texto, x, viewHeight - 25);
    }

    private void drawImageWarning(Graphics g){
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(new Color(255, 200, 0, 150));
        String warning = "⚠ Usando imagens de fallback - Verifique resources/sprites/";
        FontMetrics fm = g.getFontMetrics();
        int x = (viewWidth - fm.stringWidth(warning)) / 2;
        g.drawString(warning, x, 130);
    }

    // ============ MÉTODOS AUXILIARES ============

    private BufferedImage getCharacterImage(CharacterType type){
        switch(type){
            case WARRIOR: return warriorImage;
            case ARCHER: return archerImage;
            case MAGE: return mageImage;
            default: return warriorImage;
        }
    }

    private Color getCharacterColor(CharacterType type){
        switch(type){
            case WARRIOR: return new Color(200, 50, 50);
            case ARCHER: return new Color(50, 200, 50);
            case MAGE: return new Color(50, 100, 255);
            default: return Color.WHITE;
        }
    }
}