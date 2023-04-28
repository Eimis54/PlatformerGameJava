package entities;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] zombieArray;
    private ArrayList<Zombie> zombies = new ArrayList<>();
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();

    }

    private void addEnemies() {
        zombies = LoadSave.GetZombies();
        System.out.println("size of zombies: " + zombies.size());
    }

    public void update(int[][] lvlData){
        for(Zombie c : zombies)
            c.update(lvlData);
    }
    public void draw(Graphics g, int xLvlOffset){
        drawZombies(g, xLvlOffset);
    }

    private void drawZombies(Graphics g, int xLvlOffset) {
        for (Zombie c : zombies)
            g.drawImage(zombieArray[c.getEnemyState()][c.getAniIndex()],(int) c.getHitbox().x - xLvlOffset,(int) c.getHitbox().y, ZOMBIE_WIDTH, ZOMBIE_HEIGHT,null);
    }

    private void loadEnemyImgs() {
        zombieArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetPlayerAtlas(LoadSave.ENEMY_SPRITE);
        for(int j = 0; j < zombieArray.length;j++)
            for(int i = 0; i< zombieArray[j].length; i++)
                zombieArray[j][i]= temp.getSubimage(i*ZOMBIE_WIDTH_DEFAULT,j*ZOMBIE_HEIGHT_DEFAULT,ZOMBIE_WIDTH_DEFAULT,ZOMBIE_HEIGHT_DEFAULT);
    }
}
