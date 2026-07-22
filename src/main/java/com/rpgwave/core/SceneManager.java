package com.rpgwave.core;

import java.awt.Graphics;
import java.util.EnumMap;
import java.util.Map;

public class SceneManager {

    private final Map<GameState, GameScene> scenes = new EnumMap<>(GameState.class);
    private GameScene currentScene;
    private GameState currentState;

    public void addScene(GameState state, GameScene scene) {
        scenes.put(state, scene);
    }

    public void switchTo(GameState state) {
        GameScene next = scenes.get(state);
        if (next == null) {
            throw new IllegalArgumentException("Cena não registrada: " + state);
        }
        if (currentScene != null) {
            currentScene.onExit();
        }
        currentScene = next;
        currentState = state;
        currentScene.onEnter();
    }

    public void update() {
        if (currentScene != null) currentScene.update();
    }

    public void render(Graphics g) {
        if (currentScene != null) currentScene.render(g);
    }

    public GameState getCurrentState() { return currentState; }
}