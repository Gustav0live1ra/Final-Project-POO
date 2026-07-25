package com.rpgwave.core;

public class WaveConfig {
    public final int goblins;
    public final int aquatics;
    public final int flyers;
    public final boolean hasMiniBoss;
    public final boolean hasBigBoss;

    public WaveConfig(int goblins, int aquatics, int flyers, boolean hasMiniBoss, boolean hasBigBoss) {
        this.goblins = goblins;
        this.aquatics = aquatics;
        this.flyers = flyers;
        this.hasMiniBoss = hasMiniBoss;
        this.hasBigBoss = hasBigBoss;
    }
}