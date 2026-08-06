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

    private Character player;
    private WaveManager waveManager;
    private CopyOnWriteArrayList<Projectile> projectiles;

    private TileMap tileMap;
    private Camera camera;
    private int worldPixelWidth;
    private int worldPixelHeight;

    //Cooldown de Ataque Temporario
    private long lastBasicAttackTime = 0;
    private long lastSkillAttackTime = 0;

    private static final long BASIC_ATTACK_COOLDOWN = 300;
    private static final long SKILL_ATTACK_COOLDOWN = 700;

    public PlayingScene(InputHandler input, int viewWidth, int viewHeight,
                        CharacterType chosenCharacter) {
        this.input = input;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.chosenCharacter = chosenCharacter;
    }

    @Override
    public void onEnter() {
        projectiles = new CopyOnWriteArrayList<>();

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

        if (player.isDead()) {
            gameOver = true;
        }

        if (gameOver) {
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

        // Atualiza projéteis
        for (Projectile p : projectiles) {
            p.update(worldPixelWidth, worldPixelHeight);
        }
        projectiles.removeIf(p -> !p.isActive());

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

        // Projéteis
        for (Projectile p : projectiles) {
            p.render(g);
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

        // implementação do Game Over
        if (gameOver) {

            g.setColor(Color.RED);

            g.setFont(
                    new Font("Arial", Font.BOLD, 48)
            );

            g.drawString(
                    "GAME OVER",
                    viewWidth / 2 - 150,
                    viewHeight / 2
            );
        }
    }

    private boolean gameOver = false;
}