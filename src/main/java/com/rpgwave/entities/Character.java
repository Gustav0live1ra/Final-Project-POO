package com.rpgwave.entities;

import java.util.ArrayList;
import java.util.List;
import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.SpriteLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Character extends Entity implements Damageable {

    protected final Stats stats;
    protected final InputHandler input;
    protected final BufferedImage sprite;
    protected final List<Skill> skills;
    protected final LevelSystem levelSystem;

    // Controle de cooldown de ataque
    protected long lastAttackTime;
    protected Direction direction;


    public Character(double x, double y, int width, int height,
                     Stats stats, InputHandler input, String spritePath) {
        super(x, y, width, height);
        this.stats = stats;
        this.input = input;
        this.sprite = SpriteLoader.load(spritePath);
        this.lastAttackTime = 0;
        this.skills = new ArrayList<>();
        this.levelSystem = new LevelSystem();
        this.direction = Direction.DOWN;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        handleMovement();
        clampToBounds(worldWidth, worldHeight);
        handleAttack();
    }

    protected void handleMovement() {
        double speed = stats.getSpeed();

        if (input.isUp()) {
            position.setY(position.getY() - speed);
            direction = Direction.UP;
        }

        if (input.isDown()) {
            position.setY(position.getY() + speed);
            direction = Direction.DOWN;
        }

        if (input.isLeft()) {
            position.setX(position.getX() - speed);
            direction = Direction.LEFT;
        }

        if (input.isRight()) {
            position.setX(position.getX() + speed);
            direction = Direction.RIGHT;
        }
    }

    protected void clampToBounds(int worldWidth, int worldHeight) {
        if (position.getX() < 0) position.setX(0);
        if (position.getY() < 0) position.setY(0);
        if (position.getX() + width > worldWidth)
            position.setX(worldWidth - width);
        if (position.getY() + height > worldHeight)
            position.setY(worldHeight - height);
    }

    // Ataque MUDA pra cada personagem, então é abstrato
    protected abstract void handleAttack();

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite,
                (int) position.getX(), (int) position.getY(),
                width, height, null);
    }
    public Stats getStats() {
        return stats;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        if (skill != null) {
            skills.add(skill);
        }
    }

    public LevelSystem getLevelSystem() {
        return levelSystem;
    }

    public void addExperience(int amount) {

        int oldLevel = levelSystem.getLevel();

        int levelsGained = levelSystem.addExperience(amount);

        for (int i = 0; i < levelsGained; i++) {
            applyLevelUpBonus();
        }

        if (levelSystem.getLevel() > oldLevel) {

            System.out.println("================================");
            System.out.println("LEVEL UP!");
            System.out.println("Nível atual: " + levelSystem.getLevel());
            System.out.println("HP Máximo: " + stats.getMaxHealth());
            System.out.println("Mana Máxima: " + stats.getMaxMana());
            System.out.println("Ataque: " + stats.getAttack());
            System.out.println("Defesa: " + stats.getDefense());
            System.out.println("================================");
        }
    }

        protected void applyLevelUpBonus() {

            stats.increaseMaxHealth(20);
            stats.increaseMaxMana(10);
            stats.increaseAttack(3);
            stats.increaseDefense(2);
        }

        public Direction getDirection() {
             return direction;

    }
    @Override
    public void takeDamage(int amount) {
        stats.takeDamage(amount);
    }

    @Override
    public boolean isDead() {
        return stats.getCurrentHealth() <= 0;
    }
    }
