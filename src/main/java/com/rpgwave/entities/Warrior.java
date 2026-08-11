package com.rpgwave.entities;
import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Constants;

public class Warrior extends Character {

    public Warrior(double x, double y, InputHandler input) {

        super(
                x,
                y,
                Constants.PLAYER_WIDTH,
                Constants.PLAYER_HEIGHT,
                new Stats(150, 50, 30, 25, 4.0), input,
                Constants.PLAYER_SPRITE
        );

        addSkill(new Skill("Golpe Pesado", 40, 10
        ));
    }

    @Override
    protected void handleAttack() {
        // Ataque do Guerreiro será implementado depois
    }
}





