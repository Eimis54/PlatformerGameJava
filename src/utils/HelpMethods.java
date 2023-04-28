package utils;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData){
        if(!IsSolid(x,y,lvlData))
            if(!IsSolid(x+width,y+height,lvlData))
                if(!IsSolid(x+width,y,lvlData))
                    if(!IsSolid(x,y+height,lvlData))
                        return true;
        return false;


    }
    private static boolean IsSolid(float x, float y, int[][] lvlData){
        int maxWidth = lvlData[0].length*Game.TILE_SIZE;
        if(x < 0 || x >= maxWidth)
            return true;
        if(y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if(value >= 54 || value < 0 || value != 16)
            return true;
        return false;
    }
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x / Game.TILE_SIZE);
        if( xSpeed > 0){
            // Right
            int tileXPos = currentTile * Game.TILE_SIZE;
            int xOffset = (int)(Game.TILE_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        }else {
            // Left
            return currentTile * Game.TILE_SIZE;
        }
    }
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILE_SIZE);
        if(airSpeed > 0){
            // Falling - touching floor
            int tileYPos = currentTile * Game.TILE_SIZE;
            int yOffset = (int)(Game.TILE_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        }else{
            // Jumping
            return currentTile * Game.TILE_SIZE;
        }
    }
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // Check the pixel below bottom left and bottom right corners
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if(!IsSolid(hitbox.x+hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);

    }
}