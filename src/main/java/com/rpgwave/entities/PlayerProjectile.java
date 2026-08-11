package com.rpgwave.entities;

import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Entity {

    private final double velocityX;
    private final double velocityY;
    private final int damage;
    private final BufferedImage sprite;
    private final double angle;

    public PlayerProjectile(
            double startX,
            double startY,
            double targetX,
            double targetY,
            int damage,
            double speed,
            String spritePath) {

        super(
                startX,
                startY,
                Constants.PROJECTILE_WIDTH,
                Constants.PROJECTILE_HEIGHT
        );

        this.damage = damage;

        BufferedImage spriteSheet =
                SpriteLoader.load(spritePath);

        if (spriteSheet != null
                && spriteSheet.getWidth() >= 32
                && spriteSheet.getHeight() >= 32) {

            this.sprite =
                    spriteSheet.getSubimage(
                            0,
                            0,
                            32,
                            32
                    );

        } else {

            this.sprite = null;
        }

        double dx = targetX - startX;
        double dy = targetY - startY;

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {

            this.velocityX = 0;
            this.velocityY = 0;

        } else {

            this.velocityX =
                    (dx / distance) * speed;

            this.velocityY =
                    (dy / distance) * speed;
        }

        this.angle =
                Math.atan2(
                        this.velocityY,
                        this.velocityX
                );
    }

    @Override
    public void update(
            int worldWidth,
            int worldHeight) {

        position.setX(
                position.getX() + velocityX
        );

        position.setY(
                position.getY() + velocityY
        );

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

        if (sprite == null) {
            return;
        }

        Graphics2D g2 =
                (Graphics2D) g.create();

        try {

            int centerX =
                    (int) position.getX()
                            + width / 2;

            int centerY =
                    (int) position.getY()
                            + height / 2;

            g2.translate(
                    centerX,
                    centerY
            );
            g2.rotate(
                    angle + Math.PI / 2
            );

            int renderWidth = 32;
            int renderHeight = 32;

            g2.drawImage(
                    sprite,
                    -renderWidth / 2,
                    -renderHeight / 2,
                    renderWidth,
                    renderHeight,
                    null
            );

        } finally {

            g2.dispose();
        }
    }

        g2d.setTransform(previousTransform);
    }
}
