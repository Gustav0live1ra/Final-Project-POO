package com.rpgwave.core;

import com.rpgwave.entities.CharacterType;
import com.rpgwave.utils.Constants;

public class Game {

    private final GameCanvas canvas;
    private final Window window;
    private final InputHandler input;
    private final SceneManager sceneManager;
    private final GameLoop gameLoop;

    public Game() {
        // Canvas e janela
        canvas = new GameCanvas();
        window = new Window(canvas);

        // Input (registra listeners no canvas)
        input = new InputHandler();
        canvas.addKeyListener(input);
        canvas.addMouseListener(input);
        canvas.requestFocus();

        // Cenas
        sceneManager = new SceneManager();

        // TEMPORÁRIO: começa direto com Archer.
        // PESSOA D: implementar MenuScene → CharacterSelectScene → PlayingScene
        CharacterType defaultCharacter = CharacterType.ARCHER;
        sceneManager.addScene(GameState.PLAYING,
                new PlayingScene(input,
                        Constants.WINDOW_WIDTH,
                        Constants.WINDOW_HEIGHT, defaultCharacter));
        sceneManager.addScene(GameState.MENU,new MenuScene(input, sceneManager,
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT));
        sceneManager.addScene(GameState.CHARACTER_SELECT, new CharacterSelectScene(input, sceneManager,
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT));

        // Cenas futuras (Pessoa D):
        // sceneManager.addScene(GameState.MENU, new MenuScene(...));
        // sceneManager.addScene(GameState.PAUSED, new PauseScene(...));
        // sceneManager.addScene(GameState.GAME_OVER, new GameOverScene(...));

        sceneManager.switchTo(GameState.MENU);

        // Loop
        gameLoop = new GameLoop(this);
    }

    public void start() {
        gameLoop.start();
    }

    public void update() {
        sceneManager.update();
    }

    public void render() {
        canvas.render(sceneManager);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}