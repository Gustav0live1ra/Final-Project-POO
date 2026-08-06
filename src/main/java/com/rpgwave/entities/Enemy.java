package com.rpgwave.entities;

import com.rpgwave.utils.Animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public abstract class Enemy extends Entity implements Damageable {
    public enum State { IDLE, CHASE, ATTACK }

    protected State state = State.IDLE;
    protected BufferedImage sprite;
    protected Color placeholderColor = Color.RED;
    protected final Entity target;
    protected Map<State, Animation> animations = new EnumMap<>(State.class);
    protected final double spawnX, spawnY;
    protected double speed;
    protected double detectionRadius;
    protected double attackRadius;
    protected double territoryRadius;

    protected long lastAttackTime = 0;
    protected long attackCooldownMs = 1000;
    protected int health = 100;
    protected int defense = 10;
    protected int experienceReward = 20;

    public Enemy(double x, double y, int width, int height, Entity target,
                 double speed, double detectionRadius, double attackRadius, double territoryRadius) {
        super(x, y, width, height);
        this.spawnX = x;
        this.spawnY = y;
        this.target = target;
        this.speed = speed;
        this.detectionRadius = detectionRadius;
        this.attackRadius = attackRadius;
        this.territoryRadius = territoryRadius;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        double distToPlayer = distanceTo(target.getCenterX(), target.getCenterY());
        double distToSpawn = distanceTo(spawnX, spawnY);

        switch (state) {
            case IDLE:
                if (distToPlayer < detectionRadius) {
                    state = State.CHASE;
                }
                break;

            case CHASE:
                moveToward(target.getCenterX(), target.getCenterY());
                if (distToPlayer < attackRadius) {
                    state = State.ATTACK;
                } else if (distToSpawn > territoryRadius) {
                    state = State.IDLE;
                }
                break;

            case ATTACK:
                if (distToPlayer > attackRadius) {
                    state = State.CHASE;
                } else if (canAttack()) {
                    performAttack();
                    lastAttackTime = System.currentTimeMillis();
                }
                break;
        }
    }

    protected abstract void performAttack();

    protected boolean canAttack() {
        return (System.currentTimeMillis() - lastAttackTime) >= attackCooldownMs;
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            setActive(false);
        }
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    protected double distanceTo(double x, double y) {
        double dx = getCenterX() - x;
        double dy = getCenterY() - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected void moveToward(double targetX, double targetY) {
        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            position.setX(position.getX() + (dx / dist) * speed);
            position.setY(position.getY() + (dy / dist) * speed);
        }
    }

    @Override
    public void render(Graphics g) {
        Animation currentAnim = animations.get(state);
        if (currentAnim != null) {
            g.drawImage(currentAnim.getCurrentFrame(),
                    (int) position.getX(), (int) position.getY(), width, height, null);
        } else if (sprite != null) {
            g.drawImage(sprite, (int) position.getX(), (int) position.getY(), width, height, null);
        } else {
            g.setColor(placeholderColor);
            g.fillRect((int) position.getX(), (int) position.getY(), width, height);
        }
    }
    public int getDefense() {
        return defense;
    }

    public int getExperienceReward() {
        return experienceReward;
    }
}