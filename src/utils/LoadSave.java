package utils;

import entities.Zombie;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.ZOMBIE;

public class LoadSave {
    public static final String PLAYER_ATLAS = "spritesheet.png";
    public static final String LEVEL_ATLAS = "TileSpritesNew.png";
//    public static final String LEVEL_ONE_DATA = "testavimo_map.png";
    public static final String LEVEL_ONE_DATA = "testavimo_map_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMAGE = "background_menu.jpg";
    public static final String PLAYING_BACKGROUND_IMAGE = "background_playing.jpg";
    public static final String CLOUDS = "clouds.png";
//    public static final String ENEMY_SPRITE = "enemy.png";
    public static final String ENEMY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";




    public static BufferedImage GetPlayerAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName );

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return img;
    }
    public static ArrayList<Zombie> GetZombies(){
        BufferedImage img = GetPlayerAtlas(LEVEL_ONE_DATA);
        ArrayList<Zombie> list = new ArrayList<>();
        for(int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == ZOMBIE)
                    list.add(new Zombie(i * Game.TILE_SIZE, j * Game.TILE_SIZE));
            }
        }
            return list;
    }
    public static int[][] GetLevelData(){
        BufferedImage img = GetPlayerAtlas(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int j = 0; j < img.getHeight(); j++){
            for(int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value  = color.getRed();
                if(value >= 54)
                    value = 0;
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
