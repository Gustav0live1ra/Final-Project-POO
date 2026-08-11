package com.rpgwave.entities;

// Pessoa B: responsável pela experiência e evolução do personagem.

public class LevelSystem {

    private int level;
    private int experience;
    private int experienceToNextLevel;

    public LevelSystem() {
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public int addExperience(int amount) {

        if (amount <= 0) {
            return 0;
        }

        experience += amount;

        int levelsGained = 0;

        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            levelUp();
            levelsGained++;
        }

        return levelsGained;
    }

    private void levelUp() {
        level++;

        experienceToNextLevel = experienceToNextLevel + 50;
    }
}