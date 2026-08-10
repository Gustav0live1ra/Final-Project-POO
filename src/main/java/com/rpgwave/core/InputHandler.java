package com.rpgwave.core;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class InputHandler implements KeyListener, MouseListener {

    // fonte da verdade para o teclado
    private final Set<Integer> pressedKeys = new HashSet<>();

    // Mouse
    private int mouseX, mouseY;
    private boolean mouseClicked;

    // === Teclado (Movimentação Contínua) ===
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isUp() { return isKeyPressed(KeyEvent.VK_W) || isKeyPressed(KeyEvent.VK_UP); }
    public boolean isDown() { return isKeyPressed(KeyEvent.VK_S) || isKeyPressed(KeyEvent.VK_DOWN); }
    public boolean isLeft() { return isKeyPressed(KeyEvent.VK_A) || isKeyPressed(KeyEvent.VK_LEFT); }
    public boolean isRight() { return isKeyPressed(KeyEvent.VK_D) || isKeyPressed(KeyEvent.VK_RIGHT); }


    // === Teclado (Ações Únicas / Consumíveis) ===

    public boolean consumeKey(int keyCode) {
        if (pressedKeys.contains(keyCode)) {
            pressedKeys.remove(keyCode); // Remove do Set, garantindo que só rode 1 vez
            return true;
        }
        return false;
    }

    // Métodos de atalho que usam o consumeKey base:
    public boolean consumeSkillKey() { return consumeKey(KeyEvent.VK_Q); }
    public boolean consumeEnter()    { return consumeKey(KeyEvent.VK_ENTER); }
    public boolean consumeEscape()   { return consumeKey(KeyEvent.VK_ESCAPE); }
    public boolean consumeM()        { return consumeKey(KeyEvent.VK_M); }


    // === Mouse ===
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }

    public boolean consumeMouseClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    // === LIMPEZA (Chamado pelo SceneManager) ===
    public void clearAllInputs() {
        pressedKeys.clear(); // Esvazia a lista de teclas com 1 comando!
        mouseClicked = false;
    }

    // === Implementação das interfaces (Eventos do SO) ===
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode()); // Só adiciona e pronto!
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode()); // Se soltou a tecla, some da lista.
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseClicked = true;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}