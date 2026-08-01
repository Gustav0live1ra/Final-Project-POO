package com.rpgwave.core;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class InputHandler implements KeyListener, MouseListener {

    // Teclado
    private final Set<Integer> pressedKeys = new HashSet<>();

    // Mouse
    private int mouseX, mouseY;
    private boolean mouseClicked;

    //Skill
    private boolean qPressed;

    // Menu
    private boolean enterPressed;

    private boolean leftPressed;
    private boolean rightPressed;

    // === Teclado ===
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    // Métodos convenientes pras teclas mais usadas
    public boolean isUp() {
        return isKeyPressed(KeyEvent.VK_W) || isKeyPressed(KeyEvent.VK_UP);
    }

    public boolean isDown() {
        return isKeyPressed(KeyEvent.VK_S) || isKeyPressed(KeyEvent.VK_DOWN);
    }

    public boolean isLeft() {
        return isKeyPressed(KeyEvent.VK_A) || isKeyPressed(KeyEvent.VK_LEFT);
    }

    public boolean isRight() {
        return isKeyPressed(KeyEvent.VK_D) || isKeyPressed(KeyEvent.VK_RIGHT);
    }

    public boolean consumeSkillKey() {
        if (qPressed) {
            qPressed = false;
            return true;
        }

        return false;
    }

    // === Mouse ===
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }

    // Retorna true UMA VEZ e reseta (evita atirar 60x por segundo com um clique)
    public boolean consumeMouseClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }
    public boolean consumeEnter() {

        if (enterPressed) {
            enterPressed = false;
            return true;
        }

        return false;
    }
    public boolean consumeLeft() {
        if (leftPressed) {
            leftPressed = false;
            return true;
        }
        return false;
    }

    public boolean consumeRight() {
        if (rightPressed) {
            rightPressed = false;
            return true;
        }
        return false;
    }

    // === Implementação das interfaces ===
    @Override
    public void keyPressed(KeyEvent e) {

        pressedKeys.add(e.getKeyCode());

        if (e.getKeyCode() == KeyEvent.VK_Q) {
            qPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
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