package com.rpgwave.world;

import java.awt.image.BufferedImage;

public class Tileset {
    public final int firstGid;
    public final int tileCount;
    public final int columns;
    public final int tileWidth;
    public final int tileHeight;
    public final BufferedImage image;
    private final boolean[] blankTile;

    public Tileset(int firstGid, int tileCount, int columns, int tileWidth, int tileHeight, BufferedImage image) {
        this.firstGid = firstGid;
        this.tileCount = tileCount;
        this.columns = columns;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.image = image;
        this.blankTile = new boolean[tileCount];
        precomputeBlankTiles();
    }

    private void precomputeBlankTiles() {
        for (int i = 0; i < tileCount; i++) {
            int col = i % columns;
            int row = i / columns;
            int px = col * tileWidth;
            int py = row * tileHeight;
            if (px + tileWidth > image.getWidth() || py + tileHeight > image.getHeight()) {
                blankTile[i] = true;
                continue;
            }
            blankTile[i] = isMostlyTransparent(px, py);
        }
    }

    private boolean isMostlyTransparent(int px, int py) {
        long totalAlpha = 0;
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                int argb = image.getRGB(px + x, py + y);
                totalAlpha += (argb >>> 24) & 0xff;
            }
        }
        double avg = totalAlpha / (double) (tileWidth * tileHeight * 255);
        return avg < 0.05; // menos de 5% de opacidade média = considera "vazio"
    }

    public boolean contains(int gid) {
        return gid >= firstGid && gid < firstGid + tileCount;
    }

    public boolean isBlank(int gid) {
        int localId = gid - firstGid;
        if (localId < 0 || localId >= blankTile.length) return false;
        return blankTile[localId];
    }

    public BufferedImage getTile(int gid) {
        int localId = gid - firstGid;
        int col = localId % columns;
        int row = localId / columns;
        return image.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }
}