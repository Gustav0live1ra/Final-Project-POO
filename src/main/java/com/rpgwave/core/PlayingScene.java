package com.rpgwave.core;

import com.rpgwave.entities.*;
import com.rpgwave.entities.Character;
import com.rpgwave.world.Camera;
import com.rpgwave.world.TileMap;
import com.rpgwave.world.TmxLoader;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import com.rpgwave.entities.MageSkillProjectile;

public class PlayingScene implements GameScene {

    private static final Set<String> GROUND_LAYERS = Set.of(
            "Camada de Blocos 1", "Chão", "Agua", "agua detalhe",
            "costa", "montanhas", "Grandes"
    );

    private static final Set<String> OVERHEAD_LAYERS = Set.of(
            "detalhes animados", "detalhes", "detalhes pequenos", "mais"
    );

    private final InputHandler input;
    private final int viewWidth;
    private final int viewHeight;
    private final CharacterType chosenCharacter;
    private final SceneManager sceneManager;
    private boolean gameInitialized = false;

    private Character player;
    private WaveManager waveManager;
    private CopyOnWriteArrayList<Projectile> projectiles;
    private CopyOnWriteArrayList<AttackEffect> attackEffects;
    private CopyOnWriteArrayList<PlayerProjectile> playerProjectiles;
    private CopyOnWriteArrayList<SwordSlashEffect> swordSlashEffects;
    private CopyOnWriteArrayList<HeavySlashEffect> heavySlashEffects;
    private Hud hud;
    private CopyOnWriteArrayList<ArcherSkillProjectile> archerSkillProjectiles;
    private CopyOnWriteArrayList<MageSkillProjectile> mageSkillProjectiles;

    private TileMap tileMap;
    private Camera camera;
    private int worldPixelWidth;
    private int worldPixelHeight;

    private long lastBasicAttackTime = 0;
    private long lastSkillAttackTime = 0;

    private static final long BASIC_ATTACK_COOLDOWN = 300;
    private static final long SKILL_ATTACK_COOLDOWN = 700;

    private static final int PLAYER_PROJECTILE_SPEED = 9;

    public PlayingScene(
            InputHandler input,
            SceneManager sceneManager,
            int viewWidth,
            int viewHeight,
            CharacterType chosenCharacter) {

        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.chosenCharacter = chosenCharacter;
    }

    @Override
    public void onEnter() {
        if (gameInitialized){
            return;
        }

        gameInitialized = true;

        projectiles = new CopyOnWriteArrayList<>();
        playerProjectiles = new CopyOnWriteArrayList<>();
        swordSlashEffects = new CopyOnWriteArrayList<>();
        heavySlashEffects = new CopyOnWriteArrayList<>();
        archerSkillProjectiles = new CopyOnWriteArrayList<>();
        mageSkillProjectiles = new CopyOnWriteArrayList<>();

        tileMap = TmxLoader.load(
                "/maps/mapa_principal.tmx",
                "/maps/"
        );

        worldPixelWidth =
                tileMap.width * tileMap.tileWidth * TileMap.SCALE;

        worldPixelHeight =
                tileMap.height * tileMap.tileHeight * TileMap.SCALE;

        double[] spawn = findSafeSpawn();

        player = CharacterFactory.create(
                chosenCharacter,
                spawn[0],
                spawn[1],
                input
        );

        camera = new Camera(
                viewWidth,
                viewHeight,
                worldPixelWidth,
                worldPixelHeight
        );

        waveManager = new WaveManager(
                player,
                worldPixelWidth,
                worldPixelHeight,
                tileMap
        );

        attackEffects = new CopyOnWriteArrayList<>();
        hud = new Hud();
    }

    private double[] findSafeSpawn() {
        double centerX = worldPixelWidth / 2.0;
        double centerY = worldPixelHeight / 2.0;

        int tileSize = tileMap.tileWidth * TileMap.SCALE;

        for (int radius = 0; radius < 30; radius++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {

                    if (Math.max(Math.abs(dx), Math.abs(dy)) != radius) {
                        continue;
                    }

                    double x = centerX + dx * tileSize;
                    double y = centerY + dy * tileSize;

                    if (!tileMap.isSolidAt(x, y)) {
                        return new double[]{x, y};
                    }
                }
            }
        }

        return new double[]{centerX, centerY};
    }

    @Override
    public void onExit() {
        if (playerProjectiles != null) {
            playerProjectiles.clear();
        }
    }

    private double[] aimVectorTowardsMouse() {
        double worldMouseX = input.getMouseX() + camera.getX();
        double worldMouseY = input.getMouseY() + camera.getY();

        return new double[]{
                worldMouseX - player.getCenterX(),
                worldMouseY - player.getCenterY()
        };
    }

    private void spawnPlayerProjectile(double aimX, double aimY, Skill skill) {
        int projWidth = player.getProjectileWidth();
        int projHeight = player.getProjectileHeight();

        double startX = player.getCenterX() - projWidth / 2.0;
        double startY = player.getCenterY() - projHeight / 2.0;

        playerProjectiles.add(new PlayerProjectile(
                startX,
                startY,
                aimX,
                aimY,
                PLAYER_PROJECTILE_SPEED,
                projWidth,
                projHeight,
                player.getProjectileFrames(),
                player.getProjectileFrameDurationMs(),
                player.getProjectileBaseAngleDeg(),
                player,
                skill
        ));
    }

    private void meleeHit(Skill skill) {
        double attackRange = 100;

        for (Enemy e : waveManager.getActiveEnemies()) {

            double dx = e.getCenterX() - player.getCenterX();
            double dy = e.getCenterY() - player.getCenterY();
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist >= attackRange) continue;

            int damage = (skill != null)
                    ? DamageCalculator.calculateDamage(player.getStats().getAttack(), skill, e.getDefense())
                    : SkillManager.calculateBasicAttackDamage(player, e.getDefense());

            e.takeDamage(damage);

            if (e.isDead()) {
                player.getStats().restoreMana(5);
                player.addExperience(e.getExperienceReward());
                System.out.println("Inimigo derrotado! +" + e.getExperienceReward() + " XP");
            }

            System.out.println(
                    (skill != null ? "Skill: " + skill.getName() : "Ataque básico") +
                            " | Dano causado: " + damage +
                            " | Mana: " + player.getStats().getCurrentMana()
            );
        }
    }

    @Override
    public void update() {
        if (input.consumeEscape()){
            sceneManager.switchTo(GameState.PAUSED);
            return;
        }

        if (player.isDead()) {
            sceneManager.switchTo(GameState.GAME_OVER);
            return;
        }

        double prevX = player.getPosition().getX();
        double prevY = player.getPosition().getY();

        player.update(worldPixelWidth, worldPixelHeight);

        if (tileMap.isSolidAt(
                player.getCenterX(),
                player.getCenterY())) {

            player.getPosition().setX(prevX);
            player.getPosition().setY(prevY);
        }

        camera.follow(player);

        waveManager.update(
                worldPixelWidth,
                worldPixelHeight
        );

        // Atualiza projéteis dos inimigos
        for (Projectile p : projectiles) {
            p.update(worldPixelWidth, worldPixelHeight);
        }
        projectiles.removeIf(p -> !p.isActive());

        // Atualiza projeteis dos players
        for (PlayerProjectile p : playerProjectiles) {
            p.update(worldPixelWidth, worldPixelHeight);
        }

        playerProjectiles.removeIf(p -> !p.isActive());

        for (SwordSlashEffect effect : swordSlashEffects) {
            effect.update(
                    worldPixelWidth,
                    worldPixelHeight
            );
        }

        swordSlashEffects.removeIf(
                effect -> !effect.isActive()
        );

        for (HeavySlashEffect effect : heavySlashEffects) {

            effect.update(
                    worldPixelWidth,
                    worldPixelHeight
            );
        }

        heavySlashEffects.removeIf(
                effect -> !effect.isActive()
        );

        for (ArcherSkillProjectile p : archerSkillProjectiles) {
            p.update(
                    worldPixelWidth,
                    worldPixelHeight
            );
        }

        archerSkillProjectiles.removeIf(
                p -> !p.isActive()
        );
        for (MageSkillProjectile p : mageSkillProjectiles) {
            p.update(
                    worldPixelWidth,
                    worldPixelHeight
            );
        }

        mageSkillProjectiles.removeIf(
                p -> !p.isActive()
        );

        // Ataque temporário de teste
        if (input.consumeMouseClick()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastBasicAttackTime >= BASIC_ATTACK_COOLDOWN) {

                lastBasicAttackTime = currentTime;
            }

        // O dano só acontece AQUI — quando a flecha/bola de fogo realmente
        for (PlayerProjectile p : playerProjectiles) {
            if (!p.isActive()) continue;

            for (Enemy e : waveManager.getActiveEnemies()) {
                if (e.isDead()) continue;

                if (p.collidesWith(e)) {
                    int damage = p.computeDamageAndConsume(e.getDefense());

                    int enemydamage = SkillManager.calculateBasicAttackDamage(
                            player,
                            e.getDefense()
                    );

                    e.takeDamage(damage);

                    if (chosenCharacter == CharacterType.ARCHER) {

                        playerProjectiles.add(
                                new PlayerProjectile(
                                        player.getCenterX(),
                                        player.getCenterY(),
                                        e.getCenterX(),
                                        e.getCenterY(),
                                        damage,
                                        8,
                                        "/sprites/Arrow.png"
                                )
                        );
                    }

                    if (chosenCharacter == CharacterType.MAGE) {

                        playerProjectiles.add(
                                new PlayerProjectile(
                                        player.getCenterX(),
                                        player.getCenterY(),
                                        e.getCenterX(),
                                        e.getCenterY(),
                                        damage,
                                        6,
                                        "/sprites/Fireball.png"
                                )
                        );
                    }

                        if (chosenCharacter == CharacterType.WARRIOR) {

                            swordSlashEffects.add(
                                    new SwordSlashEffect(
                                            e.getCenterX(),
                                            e.getCenterY()
                                    )
                            );
                    }

                    // Recupera mana ao derrotar um inimigo
                    if (e.isDead()) {

                        player.getStats().restoreMana(5);

                        if (e.isDead()) {
                            player.getStats().restoreMana(5);
                            player.addExperience(e.getExperienceReward());
                            System.out.println("Inimigo derrotado! +" + e.getExperienceReward() + " XP");
                        }

                        System.out.println("Acerto! Dano causado: " + damage);
                    }

                    break;
                }
            }
        }
        playerProjectiles.removeIf(p -> !p.isActive());

        if (input.consumeMouseClick()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastSkillAttackTime < SKILL_ATTACK_COOLDOWN) {
                    lastBasicAttackTime = currentTime;
            }

            double attackRange = 100;

            for (Enemy e : waveManager.getActiveEnemies()) {

                double dx = e.getCenterX() - player.getCenterX();
                double dy = e.getCenterY() - player.getCenterY();

                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < attackRange) {

                    Skill skill = player.getSkills().get(0);

                    int damage = SkillManager.useSkillAndCalculateDamage(
                            player,
                            skill,
                            e.getDefense()
                    );

                    if (damage > 0) {
                        lastSkillAttackTime = currentTime;

                        e.takeDamage(damage);

                        if (chosenCharacter == CharacterType.WARRIOR) {

                            heavySlashEffects.add(
                                    new HeavySlashEffect(
                                            player.getCenterX(),
                                            player.getCenterY(),
                                            e.getCenterX(),
                                            e.getCenterY()
                                    )
                            );
                        }

                                if (chosenCharacter == CharacterType.ARCHER) {

                                    archerSkillProjectiles.add(
                                            new ArcherSkillProjectile(
                                                    player.getCenterX(),
                                                    player.getCenterY(),
                                                    e.getCenterX(),
                                                    e.getCenterY()
                                            )
                                    );
                                }
                        if (chosenCharacter == CharacterType.MAGE) {

                            mageSkillProjectiles.add(
                                    new MageSkillProjectile(
                                            player.getCenterX(),
                                            player.getCenterY(),
                                            e.getCenterX(),
                                            e.getCenterY()
                                    )
                            );
                        }

                        // Recupera mana e aumenta o level do player
                        if (e.isDead()) {

                player.triggerAttackAnimation(420);

                if (player.getProjectileFrames() != null) {
                    double[] aim = aimVectorTowardsMouse();
                    spawnPlayerProjectile(aim[0], aim[1], null);
                } else {
                    meleeHit(null);
                }
            }
        }

        if (input.consumeSkillKey()) {
            long currentTime = System.currentTimeMillis();
            Skill skill = player.getSkills().get(0);

            boolean offCooldown = currentTime - lastSkillAttackTime >= SKILL_ATTACK_COOLDOWN;

            if (offCooldown && SkillManager.useSkill(player, skill)) {
                lastSkillAttackTime = currentTime;

                if (player.getProjectileFrames() != null) {
                    double[] aim = aimVectorTowardsMouse();
                    spawnPlayerProjectile(aim[0], aim[1], skill);
                } else {
                    meleeHit(skill);
                }

            } else if (offCooldown) {
                System.out.println("Mana insuficiente para usar: " + skill.getName());
            }
        }
    }

    @Override
    public void render(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(
                0,
                0,
                viewWidth,
                viewHeight
        );

        tileMap.render(
                g,
                camera.getX(),
                camera.getY(),
                viewWidth,
                viewHeight,
                GROUND_LAYERS
        );

        g.translate(
                -camera.getX(),
                -camera.getY()
        );

        player.render(g);

        waveManager.render(g);

        // Projéteis Inimigos
        for (Projectile p : projectiles) {
            p.render(g);
        }

        // Projeteis Jogador
        for (PlayerProjectile p : playerProjectiles) {
            p.render(g);
        }

        for (SwordSlashEffect effect : swordSlashEffects) {
            effect.render(g);
        }

        for (HeavySlashEffect effect : heavySlashEffects) {
            effect.render(g);
        }

        for (ArcherSkillProjectile p : archerSkillProjectiles) {
            p.render(g);
        }
        for (MageSkillProjectile p : mageSkillProjectiles) {
            p.render(g);
        }

        // Volta para coordenadas da tela
        g.translate(
                camera.getX(),
                camera.getY()
        );

        tileMap.render(
                g,
                camera.getX(),
                camera.getY(),
                viewWidth,
                viewHeight,
                OVERHEAD_LAYERS
        );

        // HUD
        hud.render(
                g,
                player,
                chosenCharacter,
                waveManager.getCurrentWave(),
                waveManager.getActiveEnemies().size(),
                viewWidth,
                viewHeight
        );
    }
}
