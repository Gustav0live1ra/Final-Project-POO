package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Constants;

public class Mage extends Character {

    public Mage(double x, double y, InputHandler input) {

        super(
                x,
                y,
                Constants.PLAYER_WIDTH,
                Constants.PLAYER_HEIGHT,
                new Stats(80, 150, 35, 10, 3.5),
                input,
                Constants.PLAYER_SPRITE
        );
        addSkill(new Skill("Bola de fogo", 50, 30));
    }

    @Override
    protected void handleAttack() {
        // Ataque do Mago será implementado depois
    }
}