package com.rpgwave.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class BigBossEnemy extends Enemy {
    private boolean slamPhase = true;
    private final List<Projectile> projectiles = new ArrayList<>();

    public BigBossEnemy(double x, double y, int width, int height, Entity target) {
        super(x, y, width, height, target,
                1.0,
                5000,
                80,
                9999);
        this.placeholderColor = Color.MAGENTA;
        this.attackCooldownMs = 2000;
        this.health = 500;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        super.update(worldWidth, worldHeight);
        for (Projectile p : projectiles) {
            p.update(worldWidth, worldHeight);

            if (p.isActive() && p.collidesWith(target)) {
                if (target instanceof Damageable) {
                    ((Damageable) target).takeDamage(15);
                }
                p.setActive(false);
            }
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        for (Projectile p : projectiles) {
            p.render(g);
        }
    }

    @Override
    protected void performAttack() {
        if (slamPhase) {
            groundSlam();
        } else {
            shootProjectiles();
        }
        slamPhase = !slamPhase;
    }

    private void groundSlam() {
        if (target instanceof Damageable) {
            ((Damageable) target).takeDamage(30);
        }
    }

    private void shootProjectiles() {
        int numProjectiles = 3;
        int speed = 4;
        for (int i = 0; i < numProjectiles; i++) {
            projectiles.add(new Projectile(
                    getCenterX(), getCenterY(),
                    (int) target.getCenterX(), (int) target.getCenterY(),
                    speed
            ));
        }
    }
}