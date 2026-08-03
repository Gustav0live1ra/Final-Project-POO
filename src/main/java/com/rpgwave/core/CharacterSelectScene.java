package com.rpgwave.core;

import com.rpgwave.entities.CharacterType;

import java.awt.*;

public class CharacterSelectScene implements GameScene {
    private final SceneManager sceneManager;
    private CharacterType selectedCharacter;

    private final InputHandler input;
    private final int viewWidth;
    private final int viewHeight;


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
    }


    @Override
    public void onEnter() {

    }


    @Override
    public void onExit() {

    }


    @Override
    public void update(){

        if(input.consumeRight()){

            selectedCharacter = CharacterType.values()
                    [(selectedCharacter.ordinal() + 1)
                    % CharacterType.values().length];

        }


        if(input.consumeLeft()){

            selectedCharacter = CharacterType.values()
                    [(selectedCharacter.ordinal() - 1 + CharacterType.values().length)
                    % CharacterType.values().length];

        }
        if (input.consumeEnter()) {

            sceneManager.addScene(
                    GameState.PLAYING,
                    new PlayingScene(
                            input,
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

        g.setColor(Color.BLACK);
        g.fillRect(0,0,viewWidth,viewHeight);


        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 35));

        g.drawString(
                "Escolha seu personagem",
                200,
                100
        );


        g.setFont(new Font("Arial", Font.BOLD, 30));

        g.drawString(
                selectedCharacter.getDisplayName(),
                300,
                200
        );


        g.setFont(new Font("Arial", Font.PLAIN, 20));

        g.drawString(
                selectedCharacter.getDescription(),
                100,
                260
        );

    }
}
