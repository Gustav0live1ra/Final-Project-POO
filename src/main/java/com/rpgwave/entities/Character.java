package com.rpgwave.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import com.rpgwave.core.InputHandler;
import com.rpgwave.utils.Animation;
import com.rpgwave.utils.SpriteLoader;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Character extends Entity implements Damageable {

    public enum AnimState { IDLE, WALK, ATTACK }

    protected final Stats stats;
    protected final InputHandler input;
    protected final BufferedImage sprite;
    protected final List<Skill> skills;
    protected final LevelSystem levelSystem;
    protected final Map<AnimState, Animation> animations = new EnumMap<>(AnimState.class);

    protected AnimState animState = AnimState.IDLE;
    protected boolean isMoving = false;
    protected long attackAnimEndTime = 0;

    protected long lastAttackTime;
    protected Direction direction;

    protected BufferedImage[] projectileFrames = null;
    protected long projectileFrameDurationMs = 100;
    protected double projectileBaseAngleDeg = 0;
    protected int projectileWidth = 20;
    protected int projectileHeight = 20;

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
        updateAnimState();
    }

    protected void handleMovement() {
        double speed = stats.getSpeed();
        isMoving = false;

        if (input.isUp()) {
            position.setY(position.getY() - speed);
            direction = Direction.UP;
            isMoving = true;
        }

        if (input.isDown()) {
            position.setY(position.getY() + speed);
            direction = Direction.DOWN;
            isMoving = true;
        }

        if (input.isLeft()) {
            position.setX(position.getX() - speed);
            direction = Direction.LEFT;
            isMoving = true;
        }

        if (input.isRight()) {
            position.setX(position.getX() + speed);
            direction = Direction.RIGHT;
            isMoving = true;
        }
    }

    protected void updateAnimState() {
        if (animations.isEmpty()) return;

        if (animState == AnimState.ATTACK) {
            if (System.currentTimeMillis() >= attackAnimEndTime) {
                animState = isMoving ? AnimState.WALK : AnimState.IDLE;
            }
            return;
        }

        animState = isMoving ? AnimState.WALK : AnimState.IDLE;
    }

    public void triggerAttackAnimation(long durationMs) {
        if (animations.isEmpty() || !animations.containsKey(AnimState.ATTACK)) return;

        animState = AnimState.ATTACK;
        animations.get(AnimState.ATTACK).reset();
        attackAnimEndTime = System.currentTimeMillis() + durationMs;
    }

    protected void clampToBounds(int worldWidth, int worldHeight) {
        if (position.getX() < 0) position.setX(0);
        if (position.getY() < 0) position.setY(0);
        if (position.getX() + width > worldWidth)
            position.setX(worldWidth - width);
        if (position.getY() + height > worldHeight)
            position.setY(worldHeight - height);
    }

    protected abstract void handleAttack();

    @Override
    public void render(Graphics g) {
        int x = (int) position.getX();
        int y = (int) position.getY();

        Animation currentAnim = animations.get(animState);

        if (currentAnim == null) {
            g.drawImage(sprite, x, y, width, height, null);
            return;
        }

        BufferedImage frame = currentAnim.getCurrentFrame();

        if (direction == Direction.LEFT) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(frame, x + width, y, -width, height, null);
        } else {
            g.drawImage(frame, x, y, width, height, null);
        }
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

    public BufferedImage[] getProjectileFrames() {
        return projectileFrames;
    }

    public long getProjectileFrameDurationMs() {
        return projectileFrameDurationMs;
    }

    public double getProjectileBaseAngleDeg() {
        return projectileBaseAngleDeg;
    }

    public int getProjectileWidth() {
        return projectileWidth;
    }

    public int getProjectileHeight() {
        return projectileHeight;
    }
}
