package com.rpgwave.core;

import com.rpgwave.entities.*;
import com.rpgwave.entities.Character;
import com.rpgwave.world.Camera;
import com.rpgwave.world.TileMap;
import com.rpgwave.world.TmxLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayingScene implements GameScene {

    private static final Set<String> GROUND_LAYERS = Set.of(
            "Camada de Blocos 1", "Chão", "Agua", "agua detalhe", "costa", "montanhas", "Grandes"
    );
    private static final Set<String> OVERHEAD_LAYERS = Set.of(
            "detalhes animados", "detalhes", "detalhes pequenos", "mais"
    );

    private final InputHandler input;
    private final int viewWidth;
    private final int viewHeight;
    private final CharacterType chosenCharacter;

    private Character player;
    private Player testPlayer;
    private WaveManager waveManager;
    private CopyOnWriteArrayList<Projectile> projectiles;

    private TileMap tileMap;
    private Camera camera;
    private int worldPixelWidth, worldPixelHeight;

    public PlayingScene(InputHandler input, int viewWidth, int viewHeight,
                        CharacterType chosenCharacter) {
        this.input = input;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.chosenCharacter = chosenCharacter;
    }

    @Override
    public void onEnter() {
        projectiles = new CopyOnWriteArrayList<>();

        tileMap = TmxLoader.load("/maps/mapa_principal.tmx", "/maps/");
        worldPixelWidth = tileMap.width * tileMap.tileWidth * TileMap.SCALE;
        worldPixelHeight = tileMap.height * tileMap.tileHeight * TileMap.SCALE;

        double[] spawn = findSafeSpawn();

        testPlayer = new Player(spawn[0], spawn[1], input);
        camera = new Camera(viewWidth, viewHeight, worldPixelWidth, worldPixelHeight);
        waveManager = new WaveManager(testPlayer, worldPixelWidth, worldPixelHeight, tileMap);
    }

    private double[] findSafeSpawn() {
        double centerX = worldPixelWidth / 2.0;
        double centerY = worldPixelHeight / 2.0;
        int tileSize = tileMap.tileWidth * TileMap.SCALE;

        for (int radius = 0; radius < 30; radius++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    if (Math.max(Math.abs(dx), Math.abs(dy)) != radius) continue;

                    double x = centerX + dx * tileSize;
                    double y = centerY + dy * tileSize;

                    if (!tileMap.isSolidAt(x, y)) {
                        return new double[]{x, y};
                    }
                }
            }
        }
        return new double[]{centerX, centerY};
    }

    @Override
    public void onExit() {
        projectiles.clear();
    }

    @Override
    public void update() {
        double prevX = testPlayer.getPosition().getX();
        double prevY = testPlayer.getPosition().getY();

        testPlayer.update(worldPixelWidth, worldPixelHeight);

        if (tileMap.isSolidAt(testPlayer.getCenterX(), testPlayer.getCenterY())) {
            testPlayer.getPosition().setX(prevX);
            testPlayer.getPosition().setY(prevY);
        }

        camera.follow(testPlayer);
        waveManager.update(worldPixelWidth, worldPixelHeight);

        for (Projectile p : projectiles) {
            p.update(worldPixelWidth, worldPixelHeight);
        }
        projectiles.removeIf(p -> !p.isActive());

        if (input.consumeMouseClick()) {
            double attackRange = 60;
            for (Enemy e : waveManager.getActiveEnemies()) {
                double dx = e.getCenterX() - testPlayer.getCenterX();
                double dy = e.getCenterY() - testPlayer.getCenterY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < attackRange) {
                    e.takeDamage(50);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, viewWidth, viewHeight);

        // 1) Chão, água, montanhas, base das árvores -> desenha ANTES do personagem
        tileMap.render(g, camera.getX(), camera.getY(), viewWidth, viewHeight, GROUND_LAYERS);

        // 2) Personagem, inimigos, projéteis
        g.translate(-camera.getX(), -camera.getY());
        testPlayer.render(g);
        waveManager.render(g);
        for (Projectile p : projectiles) {
            p.render(g);
        }
        g.translate(camera.getX(), camera.getY());

        // 3) Copas de árvore, detalhes grandes -> desenha DEPOIS, cobrindo o personagem
        tileMap.render(g, camera.getX(), camera.getY(), viewWidth, viewHeight, OVERHEAD_LAYERS);

        g.setColor(Color.WHITE);
        g.drawString("Wave: " + waveManager.getCurrentWave(), 10, 20);
        g.drawString("HP: " + testPlayer.getHealth(), 10, 40);
    }
}