package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Constants;

public class Archer extends Character {

    public Archer(double x, double y, InputHandler input) {

        super(
                x,
                y,
                Constants.PLAYER_WIDTH,
                Constants.PLAYER_HEIGHT,
                new Stats(100, 80, 25, 15, 5.5),
                input,
                Constants.PLAYER_SPRITE
        );

        addSkill(new Skill("Flecha Perfurante", 35, 15));
    }

    @Override
    protected void handleAttack() {
        // Ataque do Arqueiro será implementado depois
    }
}