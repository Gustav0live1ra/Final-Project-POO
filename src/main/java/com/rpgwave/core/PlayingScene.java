package com.rpgwave.core;

import com.rpgwave.entities.*;
import com.rpgwave.entities.Character;
import com.rpgwave.world.Camera;
import com.rpgwave.world.TileMap;
import com.rpgwave.world.TmxLoader;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private TileMap tileMap;
    private Camera camera;
    private int worldPixelWidth;
    private int worldPixelHeight;


    // Cooldown de Ataque Temporário
    private long lastBasicAttackTime = 0;
    private long lastSkillAttackTime = 0;

    private static final long BASIC_ATTACK_COOLDOWN = 300;
    private static final long SKILL_ATTACK_COOLDOWN = 700;

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
        swordSlashEffects =
                new CopyOnWriteArrayList<>();
        heavySlashEffects = new CopyOnWriteArrayList<>();

        // Carrega o mapa
        tileMap = TmxLoader.load(
                "/maps/mapa_principal.tmx",
                "/maps/"
        );

        worldPixelWidth =
                tileMap.width * tileMap.tileWidth * TileMap.SCALE;

        worldPixelHeight =
                tileMap.height * tileMap.tileHeight * TileMap.SCALE;

        // Procura uma posição segura para o personagem
        double[] spawn = findSafeSpawn();

        // Cria o personagem escolhido
        player = CharacterFactory.create(
                chosenCharacter,
                spawn[0],
                spawn[1],
                input
        );


        // Cria a câmera
        camera = new Camera(
                viewWidth,
                viewHeight,
                worldPixelWidth,
                worldPixelHeight
        );

        // Cria o sistema de ondas usando o personagem como alvo
        waveManager = new WaveManager(
                player,
                worldPixelWidth,
                worldPixelHeight,
                tileMap
        );

        attackEffects = new CopyOnWriteArrayList<>();
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
        if (projectiles != null) {
            projectiles.clear();
        }
    }

    @Override
    public void update() {
        // Pausa do jogo
        if (input.consumeEscape()){
            sceneManager.switchTo(GameState.PAUSED);
            return;
        }

        if (player.isDead()) {
            sceneManager.switchTo(GameState.GAME_OVER);
            return;
        }

        // Guarda a posição anterior
        double prevX = player.getPosition().getX();
        double prevY = player.getPosition().getY();

        // Atualiza o personagem
        player.update(worldPixelWidth, worldPixelHeight);

        // Impede o personagem de atravessar áreas sólidas
        if (tileMap.isSolidAt(
                player.getCenterX(),
                player.getCenterY())) {

            player.getPosition().setX(prevX);
            player.getPosition().setY(prevY);
        }

        // Faz a câmera seguir o personagem
        camera.follow(player);

        // Atualiza inimigos e ondas
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

        // Ataque temporário de teste
        if (input.consumeMouseClick()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastBasicAttackTime >= BASIC_ATTACK_COOLDOWN) {

                lastBasicAttackTime = currentTime;
            }

            lastBasicAttackTime = currentTime;

            int damage = SkillManager.calculateBasicAttackDamage(
                    player,
                    0
            );

            double attackRange = 100;

            for (Enemy e : waveManager.getActiveEnemies()) {

                double dx = e.getCenterX() - player.getCenterX();
                double dy = e.getCenterY() - player.getCenterY();

                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < attackRange) {

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

                        player.addExperience(e.getExperienceReward());

                        System.out.println(
                                "Inimigo derrotado! +" +
                                        e.getExperienceReward() +
                                        " XP"
                        );
                    }

                    System.out.println(
                            "Ataque básico" +
                                    " | Dano causado: " + damage +
                                    " | Mana: " +
                                    player.getStats().getCurrentMana()
                    );
                }
            }
        }

        if (input.consumeSkillKey()) {

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

                            new HeavySlashEffect(
                                    player.getCenterX(),
                                    player.getCenterY(),
                                    e.getCenterX(),
                                    e.getCenterY()
                            );
                        }

                        // Recupera mana e aumenta o level do player
                        if (e.isDead()) {

                            player.getStats().restoreMana(5);

                            player.addExperience(e.getExperienceReward());

                            System.out.println(
                                    "Inimigo derrotado! +" +
                                            e.getExperienceReward() +
                                            " XP"
                            );
                        }

                        System.out.println(
                                "Skill: " + skill.getName() +
                                        " | Dano causado: " + damage +
                                        " | Mana restante: " +
                                        player.getStats().getCurrentMana()
                        );

                    } else {

                        System.out.println(
                                "Mana insuficiente para usar: "
                                        + skill.getName()
                        );
                    }
                }
            }
        }
    }


    @Override
    public void render(Graphics g) {

        // Fundo
        g.setColor(Color.BLACK);
        g.fillRect(
                0,
                0,
                viewWidth,
                viewHeight
        );

        // Desenha o chão
        tileMap.render(
                g,
                camera.getX(),
                camera.getY(),
                viewWidth,
                viewHeight,
                GROUND_LAYERS
        );

        // Entra no espaço do mundo
        g.translate(
                -camera.getX(),
                -camera.getY()
        );

        // Personagem
        player.render(g);

        // Inimigos
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

        // Volta para coordenadas da tela
        g.translate(
                camera.getX(),
                camera.getY()
        );

        // Desenha detalhes que ficam por cima
        tileMap.render(
                g,
                camera.getX(),
                camera.getY(),
                viewWidth,
                viewHeight,
                OVERHEAD_LAYERS
        );

        // HUD
        g.setColor(Color.WHITE);

        g.drawString(
                "Personagem: "
                        + chosenCharacter.getDisplayName(),
                10,
                20
        );

        g.drawString(
                "Wave: "
                        + waveManager.getCurrentWave(),
                10,
                40
        );

        g.drawString(
                "HP: "
                        + player.getStats().getCurrentHealth()
                        + "/"
                        + player.getStats().getMaxHealth(),
                10,
                60
        );
    }

    private boolean gameOver = false;
}