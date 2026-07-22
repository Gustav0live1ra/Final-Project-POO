# 🎮 POO JavaRPG

Jogo de RPG de ação em tempo real com sistema de waves, desenvolvido como projeto final da disciplina de Programação Orientada a Objetos.

## 📖 Sobre o Jogo

O jogador escolhe entre 3 personagens (Guerreiro, Arqueiro ou Mago) e enfrenta ondas de inimigos que ficam cada vez mais difíceis. Cada personagem tem um estilo de ataque e atributos únicos.

**Controles:**
- `WASD` — Movimentação
- `Mouse` — Mira e ataque
- `ESC` — Pausar (a implementar)

---

## 🛠️ Tecnologias

- **Java 17**
- **Swing** (JFrame + Canvas) para renderização
- **IntelliJ IDEA** como IDE 

Sem dependências externas! É só clonar e rodar.

---

## 🚀 Como Rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU-USUARIO/POO-JavaRPG.git

2. Abra a pasta no IntelliJ IDEA


3. Configure o SDK como Java 17:

    File → Project Structure → Project → SDK: Java 17

    **Marque as pastas corretamente (se não estiverem):**
    
        src/main/java → Sources Root (azul)

        resources → Resources Root (amarelo)


4. Rode a classe Game.java (pacote com.rpgwave.core)


## 🧩 Como a Arquitetura Funciona

    Fluxo básico

    Game (orquestrador)
        ├─ Window (JFrame)
        ├─ GameCanvas (área de desenho)
        ├─ InputHandler (teclado + mouse)
        ├─ GameLoop (thread com ~60 FPS)
        └─ SceneManager (troca de telas)
            └─ GameScene (interface)
                ├─ MenuScene       [A implementar]
                ├─ CharacterSelectScene [A implementar]
                ├─ PlayingScene    [existe, expandir]
                ├─ PauseScene      [A implementar]
                └─ GameOverScene   [A implementar]

## Hierarquia de Entidades

    Entity (abstrata)
        ├─ Projectile
        └─ Character (abstrata)
            ├─ Warrior  → ataque corpo a corpo
            ├─ Archer   → atira flechas
            ├─ Mage     → lança magias
            └─ Enemy    [A implementar]


## 👨‍💻 Equipe

    [Gustavo] — Arquitetura e Núcleo
    [Élison] — Sistema de RPG e Combate
    [G.Gabriel] — Mundo, sistema de Waves
    [Emanuel] — Interface e Suporte