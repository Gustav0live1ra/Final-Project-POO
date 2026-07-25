package com.rpgwave.entities;

public interface Damageable {
    void takeDamage(int amount);
    boolean isDead();
}