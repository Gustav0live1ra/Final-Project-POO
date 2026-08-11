package com.rpgwave.core;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    private final Set<Integer> pressedKeys = new HashSet<>();

    private int mouseX, mouseY;
    private boolean mouseClicked;

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isUp() { return isKeyPressed(KeyEvent.VK_W) || isKeyPressed(KeyEvent.VK_UP); }
    public boolean isDown() { return isKeyPressed(KeyEvent.VK_S) || isKeyPressed(KeyEvent.VK_DOWN); }
    public boolean isLeft() { return isKeyPressed(KeyEvent.VK_A) || isKeyPressed(KeyEvent.VK_LEFT); }
    public boolean isRight() { return isKeyPressed(KeyEvent.VK_D) || isKeyPressed(KeyEvent.VK_RIGHT); }

    public boolean consumeKey(int keyCode) {
        if (pressedKeys.contains(keyCode)) {
            pressedKeys.remove(keyCode);
            return true;
        }
        return false;
    }

    public boolean consumeSkillKey() { return consumeKey(KeyEvent.VK_Q); }
    public boolean consumeEnter()    { return consumeKey(KeyEvent.VK_ENTER); }
    public boolean consumeEscape()   { return consumeKey(KeyEvent.VK_ESCAPE); }
    public boolean consumeM()        { return consumeKey(KeyEvent.VK_M); }

    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }

    public boolean consumeMouseClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    public void clearAllInputs() {
        pressedKeys.clear();
        mouseClicked = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
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

    // Rastreiam a posição do mouse mesmo sem clicar, pra mira em tempo real
    // (flecha do Arqueiro, bola de fogo do Mago, etc. seguem o cursor).
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
