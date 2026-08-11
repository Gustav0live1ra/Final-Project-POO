package com.rpgwave.utils;

public final class Constants {

    private Constants() {}

    // Janela
    public static final String GAME_TITLE = "POO JavaRPG";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // Game Loop
    public static final int TARGET_FPS = 60;
    public static final long FRAME_TIME_MS = 1000 / TARGET_FPS; // ~16ms

    // Player
    public static final int PLAYER_WIDTH = 45;
    public static final int PLAYER_HEIGHT = 45;
    public static final int PLAYER_SPEED = 3;
    public static final String PLAYER_SPRITE = "/sprites/Tank.png";

    // Projéteis
    public static final int PROJECTILE_WIDTH = 15;
    public static final int PROJECTILE_HEIGHT = 15;
    public static final int PROJECTILE_SPEED = 5;
    public static final String PROJECTILE_SPRITE = "/sprites/projectile.png";

    // Debug
    public static final boolean DEBUG_MODE = false;

    //background sounds
    public static final String MENU_SOUND = "/audio/sound_menu.wav";
    public static final String GAMEPLAY_SOUND = "/audio/sound_gameplay.wav";
}