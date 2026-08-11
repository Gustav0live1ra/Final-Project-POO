package com.rpgwave.entities;

import com.rpgwave.utils.Animation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Entity {

    private final double vx, vy;
    private final Animation animation;
    private final double spriteBaseAngleDeg;

    private final Character owner;
    private final Skill skill;
    private boolean hasHit = false;

    public PlayerProjectile(
            double startX,
            double startY,
            double dirX,
            double dirY,
            int speed,
            int width,
            int height,
            BufferedImage[] frames,
            long frameDurationMs,
            double spriteBaseAngleDeg,
            Character owner,
            Skill skill) {

        super(startX, startY, width, height);

        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) {
            dirX = 1;
            dirY = 0;
            len = 1;
        }

        this.vx = (dirX / len) * speed;
        this.vy = (dirY / len) * speed;

        this.animation = (frames != null && frames.length > 0)
                ? new Animation(frames, frameDurationMs)
                : null;
        this.spriteBaseAngleDeg = spriteBaseAngleDeg;
        this.owner = owner;
        this.skill = skill;
    }

    @Override
    public void update(int worldWidth, int worldHeight) {

        position.setX(position.getX() + vx);
        position.setY(position.getY() + vy);

        if (position.getX() + width < 0
                || position.getX() > worldWidth
                || position.getY() + height < 0
                || position.getY() > worldHeight) {

            active = false;
        }
    }

    public int computeDamageAndConsume(int enemyDefense) {
        if (hasHit || owner == null) {
            return 0;
        }
        hasHit = true;
        active = false;

        if (skill != null) {
            return DamageCalculator.calculateDamage(
                    owner.getStats().getAttack(), skill, enemyDefense);
        }

        return SkillManager.calculateBasicAttackDamage(owner, enemyDefense);
    }

    private double directionAngleDeg() {
        return Math.toDegrees(Math.atan2(vy, vx));
    }

    @Override
    public void render(Graphics g) {

        if (animation == null) {
            g.setColor(Color.YELLOW);
            g.fillOval((int) position.getX(), (int) position.getY(), width, height);
            return;
        }

        BufferedImage frame = animation.getCurrentFrame();
        double rotationDeg = directionAngleDeg() - spriteBaseAngleDeg;

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform previousTransform = g2d.getTransform();

        double centerX = position.getX() + width / 2.0;
        double centerY = position.getY() + height / 2.0;

        g2d.translate(centerX, centerY);
        g2d.rotate(Math.toRadians(rotationDeg));
        g2d.drawImage(frame, -width / 2, -height / 2, width, height, null);

        g2d.setTransform(previousTransform);
    }
}
