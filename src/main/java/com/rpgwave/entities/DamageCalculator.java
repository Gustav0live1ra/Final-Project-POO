package com.rpgwave.entities;

// Pessoa B: responsável pelo cálculo do dano dos ataques.

public class DamageCalculator {

    public static int calculateDamage(
            int attack,
            Skill skill,
            int defense) {

        if (skill == null) {
            return 0;
        }

        int damage = attack + skill.getDamage() - defense;

        if (damage < 0) {
            damage = 0;
        }

        return damage;
    }
}