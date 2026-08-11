package com.rpgwave.entities;

//Pessoa B: Atributos base de qualquer personagem(vida, velocidade, dano, etc)

public class Stats {

    private int maxHealth;
    private int currentHealth;
    private int maxMana;
    private int currentMana;
    private int attack;
    private int defense;
    private double speed;



    public Stats(int maxHealth, int maxMana, int attack, int defense, double speed) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;

    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }

        currentHealth -= damage;

        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void heal(int amount){
        if (amount <= 0) {
            return;
        }

        currentHealth += amount;

         if (currentHealth > maxHealth){
             currentHealth = maxHealth;
        }
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public boolean useMana(int amount){
        if (amount <= 0) {
            return false;
        }

        if (currentMana >= amount) {
            currentMana -= amount;
            return true;
        }

        return false;
    }

    public void restoreMana(int amount) {
        if (amount <= 0) {
            return;
            
        }

        currentMana += amount;

        if (currentMana > maxMana) {
            currentMana = maxMana;
        }
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public double getSpeed() {
        return speed;
    }

    public void increaseMaxHealth(int amount) {
        if (amount <= 0) {
            return;
        }

        maxHealth += amount;
        currentHealth += amount;
    }

    public void increaseMaxMana(int amount) {
        if (amount <= 0) {
            return;
        }

        maxMana += amount;
        currentMana += amount;
    }

    public void increaseAttack(int amount) {
        if (amount <= 0) {
            return;
        }

        attack += amount;
    }

    public void increaseDefense(int amount) {
        if (amount <= 0) {
            return;
        }

        defense += amount;
    }







}

