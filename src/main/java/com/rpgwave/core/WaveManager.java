package com.rpgwave.core;

import com.rpgwave.entities.*;
import com.rpgwave.world.TileMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {

    private final Entity target;
    private final TileMap tileMap;
    private final List<Enemy> activeEnemies = new ArrayList<>();
    private final Random random = new Random();

    private int currentWaveIndex = 0;
    private long waveStartTime;
    private long timeBetweenWavesMs = 5000;
    private boolean waitingNextWave = true;

    private final int worldWidth;
    private final int worldHeight;

    public WaveManager(Entity target, int worldWidth, int worldHeight, TileMap tileMap) {
        this.target = target;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.tileMap = tileMap;
        this.waveStartTime = System.currentTimeMillis();
    }

    private WaveConfig getConfigFor(int waveIndex) {
        int wave = waveIndex + 1;

        boolean bigBoss = wave % 5 == 0;
        boolean miniBoss = !bigBoss;

        if (wave == 1) {
            return new WaveConfig(10, 0, 0, miniBoss, bigBoss);
        } else if (wave == 2) {
            return new WaveConfig(8, 4, 0, miniBoss, bigBoss);
        } else if (wave == 3) {
            return new WaveConfig(6, 10, 0, miniBoss, bigBoss);
        } else if (wave == 4) {
            return new WaveConfig(6, 8, 4, miniBoss, bigBoss);
        } else {
            int base = wave - 4;
            return new WaveConfig(6 + base, 6 + base, 4 + base, miniBoss, bigBoss);
        }
    }

    public void update(int worldWidth, int worldHeight) {
        activeEnemies.removeIf(e -> !e.isActive());

        if (waitingNextWave) {
            long elapsed = System.currentTimeMillis() - waveStartTime;
            if (elapsed >= timeBetweenWavesMs) {
                spawnWave();
                waitingNextWave = false;
            }
        } else if (activeEnemies.isEmpty()) {
            currentWaveIndex++;
            waveStartTime = System.currentTimeMillis();
            waitingNextWave = true;
        }

        for (Enemy e : activeEnemies) {
            double prevX = e.getPosition().getX();
            double prevY = e.getPosition().getY();

            e.update(worldWidth, worldHeight);

            if (tileMap.isSolidAt(e.getCenterX(), e.getCenterY())) {
                e.getPosition().setX(prevX);
                e.getPosition().setY(prevY);
            }
        }
    }

    private void spawnWave() {
        WaveConfig config = getConfigFor(currentWaveIndex);

        for (int i = 0; i < config.goblins; i++) activeEnemies.add(spawnAt(GoblinEnemy.class));
        for (int i = 0; i < config.aquatics; i++) activeEnemies.add(spawnAt(AquaticEnemy.class));
        for (int i = 0; i < config.flyers; i++) activeEnemies.add(spawnAt(FlyingEnemy.class));

        if (config.hasBigBoss) {
            activeEnemies.add(new BigBossEnemy(worldWidth / 2.0, worldHeight / 2.0, 96, 96, target));
        }
    }

    private Enemy spawnAt(Class<? extends Enemy> type) {
        double[] pos = findValidSpawnPosition(type);
        double x = pos[0];
        double y = pos[1];

        if (type == GoblinEnemy.class) return new GoblinEnemy(x, y, 90, 72, target);
        if (type == AquaticEnemy.class) return new AquaticEnemy(x, y, 90, 59, target);
        return new FlyingEnemy(x, y, 90, 48, target);
    }

    private double[] findValidSpawnPosition(Class<? extends Enemy> type) {
        boolean canSpawnInWater = (type == FlyingEnemy.class);

        for (int attempt = 0; attempt < 50; attempt++) {
            double x = random.nextInt(worldWidth);
            double y = random.nextInt(worldHeight);

            if (tileMap.isObstacleAt(x, y)) continue;
            if (tileMap.isWaterAt(x, y) && !canSpawnInWater) continue;

            return new double[]{x, y};
        }

        return new double[]{worldWidth / 2.0, worldHeight / 2.0};
    }

    public void render(java.awt.Graphics g) {
        for (Enemy e : activeEnemies) e.render(g);
    }

    public List<Enemy> getActiveEnemies() { return activeEnemies; }
    public int getCurrentWave() { return currentWaveIndex + 1; }
}