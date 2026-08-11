package com.rpgwave.entities;

import com.rpgwave.utils.Constants;
import com.rpgwave.utils.SpriteLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Entity {

    private final double velocityX;
    private final double velocityY;
    private final double angle;

    // Suporte a sprite único (construtor antigo) ou animação de frames (novo)
    private final BufferedImage sprite;
    private final BufferedImage[] frames;
    private final long frameDurationMs;
    private final double baseAngleDeg;

    // Referência ao dono e à skill usada (para dano dinâmico)
    private final Character owner;
    private final Skill skill;

    // Dano fixo (usado apenas no construtor legado)
    private final int fixedDamage;
    private final boolean useFixedDamage;

    // Controle de estado
    private boolean hasHit = false;
    private long spawnTimeMs;

    /**
     * Construtor legado (dano fixo + sprite simples).
     * Mantido por compatibilidade com código antigo que ainda usa
     * new PlayerProjectile(x, y, tx, ty, damage, speed, "path.png").
     */
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

        this.fixedDamage = damage;
        this.useFixedDamage = true;
        this.owner = null;
        this.skill = null;
        this.frames = null;
        this.frameDurationMs = 0;
        this.baseAngleDeg = 0;

        BufferedImage spriteSheet = SpriteLoader.load(spritePath);

        if (spriteSheet != null
                && spriteSheet.getWidth() >= 32
                && spriteSheet.getHeight() >= 32) {

            this.sprite = spriteSheet.getSubimage(0, 0, 32, 32);
        } else {
            this.sprite = null;
        }

        double dx = targetX - startX;
        double dy = targetY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            this.velocityX = (dx / distance) * speed;
            this.velocityY = (dy / distance) * speed;
        }

        this.angle = Math.atan2(this.velocityY, this.velocityX);
        this.spawnTimeMs = System.currentTimeMillis();
    }

    /**
     * Construtor novo (dano dinâmico via owner+skill, animação por frames,
     * tamanho customizado e mira em direção livre).
     * Aqui aimX/aimY são o VETOR de mira relativo (não a posição absoluta).
     */
    public PlayerProjectile(
            double startX,
            double startY,
            double aimX,
            double aimY,
            double speed,
            int width,
            int height,
            BufferedImage[] frames,
            long frameDurationMs,
            double baseAngleDeg,
            Character owner,
            Skill skill) {

        super(startX, startY, width, height);

        this.fixedDamage = 0;
        this.useFixedDamage = false;
        this.owner = owner;
        this.skill = skill;
        this.frames = frames;
        this.frameDurationMs = Math.max(1, frameDurationMs);
        this.baseAngleDeg = baseAngleDeg;
        this.sprite = null;

        double distance = Math.sqrt(aimX * aimX + aimY * aimY);

        if (distance == 0) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            this.velocityX = (aimX / distance) * speed;
            this.velocityY = (aimY / distance) * speed;
        }

        this.angle = Math.atan2(this.velocityY, this.velocityX);
        this.spawnTimeMs = System.currentTimeMillis();
    }

    @Override
    public void update(int worldWidth, int worldHeight) {
        position.setX(position.getX() + velocityX);
        position.setY(position.getY() + velocityY);

        if (position.getX() + width < 0
                || position.getX() > worldWidth
                || position.getY() + height < 0
                || position.getY() > worldHeight) {

            active = false;
        }
    }

    /**
     * Verifica colisão simples via retângulos entre este projétil e um inimigo.
     */
    public boolean collidesWith(Enemy e) {
        if (!active || hasHit || e == null || e.isDead()) {
            return false;
        }

        double px = position.getX();
        double py = position.getY();
        double ex = e.getPosition().getX();
        double ey = e.getPosition().getY();

        return px < ex + e.getWidth()
                && px + width > ex
                && py < ey + e.getHeight()
                && py + height > ey;
    }

    /**
     * Calcula o dano deste projétil contra a defesa do alvo,
     * marca como já-usado e desativa o projétil.
     */
    public int computeDamageAndConsume(int enemyDefense) {
        if (hasHit) {
            return 0;
        }
        hasHit = true;
        active = false;

        // Modo legado: dano fixo definido no construtor
        if (useFixedDamage || owner == null) {
            return fixedDamage;
        }

        // Modo novo: dano dinâmico com base no owner e na skill
        if (skill != null) {
            return DamageCalculator.calculateDamage(
                    owner.getStats().getAttack(),
                    skill,
                    enemyDefense
            );
        }

        return SkillManager.calculateBasicAttackDamage(owner, enemyDefense);
    }

    private double directionAngleDeg() {
        return Math.toDegrees(Math.atan2(velocityY, velocityX));
    }

    private BufferedImage currentFrame() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        long elapsed = System.currentTimeMillis() - spawnTimeMs;
        int index = (int) ((elapsed / frameDurationMs) % frames.length);
        return frames[index];
    }

    @Override
    public void render(Graphics g) {
        BufferedImage toDraw = (frames != null) ? currentFrame() : sprite;
        if (toDraw == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            int centerX = (int) position.getX() + width / 2;
            int centerY = (int) position.getY() + height / 2;

            g2.translate(centerX, centerY);

            // Se tem frames animados usa baseAngleDeg como offset,
            // caso contrário usa o offset padrão de 90° (sprite apontando pra cima)
            double rotation = (frames != null)
                    ? angle + Math.toRadians(baseAngleDeg)
                    : angle + Math.PI / 2;

            g2.rotate(rotation);

            g2.drawImage(
                    toDraw,
                    -width / 2,
                    -height / 2,
                    width,
                    height,
                    null
            );
        } finally {
            g2.dispose();
        }
    }
}