package com.rpgwave.core;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene implements GameScene {
    private final InputHandler input;
    private final SceneManager sceneManager;
    private final int viewWidth;
    private final int viewHeight;
    private int selectedOption = 0;

    public MenuScene(InputHandler input, SceneManager sceneManager, int viewWidth, int viewHeight){
        this.input = input;
        this.sceneManager = sceneManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

    }
    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void update() {

        if (input.consumeKey(KeyEvent.VK_DOWN)) {
            selectedOption = (selectedOption + 1) % 2;
        }

        if (input.consumeKey(KeyEvent.VK_UP)) {
            selectedOption = (selectedOption - 1 + 2) % 2;
        }

        if (input.consumeEnter()) {

            switch (selectedOption) {

                case 0:
                    sceneManager.switchTo(GameState.CHARACTER_SELECT);
                    break;

                case 1:
                    System.exit(0);
                    break;
            }

        }

    }

    @Override
    public void render(Graphics g) {

            drawBackground(g);

            drawTitle(g);

            drawMenu(g);

            drawFooter(g);

        }
    private void drawBackground(Graphics g){

        g.setColor(new Color(18,18,18));

        g.fillRect(0,0,viewWidth,viewHeight);

    }
    private void drawTitle(Graphics g){

        String titulo="RPG WAVE";

        g.setFont(new Font("Serif",Font.BOLD,60));

        FontMetrics fm=g.getFontMetrics();

        int x=(viewWidth-fm.stringWidth(titulo))/2;

        g.setColor(Color.DARK_GRAY);
        g.drawString(titulo,x+3,123);

        g.setColor(Color.WHITE);
        g.drawString(titulo,x,120);

        String subtitulo="Survival Arena";

        g.setFont(new Font("Arial",Font.PLAIN,24));

        fm=g.getFontMetrics();

        x=(viewWidth-fm.stringWidth(subtitulo))/2;

        g.setColor(Color.LIGHT_GRAY);

        g.drawString(subtitulo,x,165);

    }
    private void drawMenu(Graphics g){

        g.setFont(new Font("Arial",Font.BOLD,30));

        drawOption(g,"Jogar",0,290);

        drawOption(g,"Sair",1,350);

    }
    private void drawOption(
            Graphics g,
            String text,
            int option,
            int y
    ){

        String value;

        if(selectedOption==option){

            value="▶ "+text;

            g.setColor(Color.YELLOW);

        }else{

            value="   "+text;

            g.setColor(Color.WHITE);

        }

        FontMetrics fm=g.getFontMetrics();

        int x=(viewWidth-fm.stringWidth(value))/2;

        g.drawString(value,x,y);

    }
    private void drawFooter(Graphics g){

        g.setFont(new Font("Arial",Font.PLAIN,16));

        g.setColor(Color.GRAY);

        String texto="↑ ↓ Navegar    ENTER Confirmar";

        FontMetrics fm=g.getFontMetrics();

        int x=(viewWidth-fm.stringWidth(texto))/2;

        g.drawString(texto,x,viewHeight-30);

    }

}
