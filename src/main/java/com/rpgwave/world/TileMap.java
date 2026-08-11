package com.rpgwave.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileMap {
    public static final int SCALE = 3;

    public final int width;
    public final int height;
    public final int tileWidth;
    public final int tileHeight;
    public final Map<String, int[][]> layers;
    public final List<Tileset> tilesets;

    public TileMap(int width, int height, int tileWidth, int tileHeight,
                   Map<String, int[][]> layers, List<Tileset> tilesets) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.layers = layers;
        this.tilesets = tilesets;
    }

    private Tileset findTileset(int gid) {
        for (Tileset ts : tilesets) {
            if (ts.contains(gid)) return ts;
        }
        return null;
    }

    public BufferedImage getTileImage(int gid) {
        if (gid == 0) return null;
        Tileset ts = findTileset(gid);
        return ts != null ? ts.getTile(gid) : null;
    }

    // Um tile só conta como obstáculo se existir E não for "vazio" (transparente)
    private boolean isBlockingTile(int gid) {
        if (gid == 0) return false;
        Tileset ts = findTileset(gid);
        if (ts == null) return false; // tileset não carregado -> não bloqueia, só não desenha
        return !ts.isBlank(gid);
    }

    public void render(Graphics g, int camX, int camY, int viewW, int viewH, Set<String> layerNames) {
        int scaledTileW = tileWidth * SCALE;
        int scaledTileH = tileHeight * SCALE;

        int startCol = Math.max(0, camX / scaledTileW);
        int endCol = Math.min(width - 1, (camX + viewW) / scaledTileW + 1);
        int startRow = Math.max(0, camY / scaledTileH);
        int endRow = Math.min(height - 1, (camY + viewH) / scaledTileH + 1);

        for (Map.Entry<String, int[][]> entry : layers.entrySet()) {
            if (layerNames != null && !layerNames.contains(entry.getKey())) continue;
            int[][] grid = entry.getValue();

            for (int row = startRow; row <= endRow; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    int gid = grid[row][col];
                    BufferedImage tile = getTileImage(gid);
                    if (tile != null) {
                        int screenX = col * scaledTileW - camX;
                        int screenY = row * scaledTileH - camY;
                        g.drawImage(tile, screenX, screenY, scaledTileW, scaledTileH, null);
                    }
                }
            }
        }
    }

    public boolean isSolidAt(double worldX, double worldY) {
        int scaledTileW = tileWidth * SCALE;
        int scaledTileH = tileHeight * SCALE;
        int col = (int) (worldX / scaledTileW);
        int row = (int) (worldY / scaledTileH);
        if (col < 0 || row < 0 || col >= width || row >= height) return true;

        int[][] montanhas = layers.get("montanhas");
        if (montanhas != null && isBlockingTile(montanhas[row][col])) return true;

        int[][] agua = layers.get("Agua");
        if (agua != null && isBlockingTile(agua[row][col])) return true;

        int[][] grandes = layers.get("Grandes");
        if (grandes != null && isBlockingTile(grandes[row][col])) return true;

        return false;
    }

    public boolean isWaterAt(double worldX, double worldY) {
        int scaledTileW = tileWidth * SCALE;
        int scaledTileH = tileHeight * SCALE;
        int col = (int) (worldX / scaledTileW);
        int row = (int) (worldY / scaledTileH);
        if (col < 0 || row < 0 || col >= width || row >= height) return false;

        int[][] agua = layers.get("Agua");
        return agua != null && isBlockingTile(agua[row][col]);
    }

    public boolean isObstacleAt(double worldX, double worldY) {
        int scaledTileW = tileWidth * SCALE;
        int scaledTileH = tileHeight * SCALE;
        int col = (int) (worldX / scaledTileW);
        int row = (int) (worldY / scaledTileH);
        if (col < 0 || row < 0 || col >= width || row >= height) return true;

        int[][] montanhas = layers.get("montanhas");
        if (montanhas != null && isBlockingTile(montanhas[row][col])) return true;

        int[][] grandes = layers.get("Grandes");
        if (grandes != null && isBlockingTile(grandes[row][col])) return true;

        return false;
    }
}