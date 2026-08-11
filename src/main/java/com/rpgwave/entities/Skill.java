package com.rpgwave.entities;

public class Skill {

    private final String name;
    private final int damage;
    private final int manaCost;

    public Skill(String name, int damage, int manaCost) {
        this.name = name;
        this.damage = damage;
        this.manaCost = manaCost;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }
}