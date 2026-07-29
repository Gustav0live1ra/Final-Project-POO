package com.rpgwave.entities;

import com.rpgwave.core.InputHandler;

public class CharacterFactory {

    public static Character create(
            CharacterType type,
            double x,
            double y,
            InputHandler input) {

        return switch (type) {
            case WARRIOR -> new Warrior(x, y, input);
            case ARCHER -> new Archer(x, y, input);
            case MAGE -> new Mage(x, y, input);
        };
    }
}