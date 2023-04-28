package levels;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;
    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[54];
        for(int j = 0; j < 9; j++){
            for(int i = 0; i < 6; i++){
                int index = j*6 + i;
                levelSprite[index] = img.getSubimage(i*16, j * 16,16,16);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset){
        for(int j = 0; j < Game.TILE_IN_HEIGHT; j++)
            for(int i = 0; i < levelOne.getLevelData()[0].length; i++){
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILE_SIZE*i - lvlOffset, Game.TILE_SIZE*j, Game.TILE_SIZE, Game.TILE_SIZE, null);
            }
    }
    public  void update(){

    }
    public Level getCurrentLevel(){
        return levelOne;
    }
}
