package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;


import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;



public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;
    public GamePanel(Game game){
        mouseInputs=new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }
    public void updateGame(){

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        game.render(g);

    }
    public Game getGame(){
        return game;
    }
}
