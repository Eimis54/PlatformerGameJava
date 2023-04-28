package entities;

import main.Game;
import utils.LoadSave;

import java.lang.Math;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Directions.*;
import static utils.Constants.Directions.DOWN;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity {
    private int animationTick, animationIndex, animationSpeed = 45;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false, attacking = false;
    private boolean left, up, right,down, jump;

    private BufferedImage[][] animations;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 3 * Game.SCALE;
    private float yDrawOffset = 16 * Game.SCALE;

    //Jumping, Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x,y,(int)(20 * Game.SCALE),(int)(29.5 * Game.SCALE));

    }
    public void update(){
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }
    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset, (int)(hitbox.y - yDrawOffset), width,height, null);
//        drawHitbox(g, xLvlOffset);
    }
    private void updateAnimationTick() {

        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(playerAction)){
                animationIndex = 0;
                attacking = false;
            }
        }
    }
    private void setAnimation() {
        int startAnimation = playerAction;

        if(moving){
            playerAction=RUNNING;
        }else{
            playerAction=IDLE;
        }
        if(inAir){
            if(airSpeed < 0){
                playerAction = JUMP;
            }
        }
        if(attacking){
            playerAction = ATTACK_2;
        }
        if(moving && attacking){
            playerAction = ATTACK_RUNNING;
        }
        if(startAnimation != playerAction){
            resetAnimationTick();
        }
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePosition() {

        moving = false;

        if(jump){
            jump();
        }
//        if(!left && !right && !inAir)
//            return;
        if(!inAir)
            if((!left && !right)|| (left && right))
                return;

        float xSpeed = 0;

        if(left)
            xSpeed -= playerSpeed;
        else if(right)
            xSpeed += playerSpeed;

        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }

        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width,hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        }else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed,hitbox.y, hitbox.width,hitbox.height,lvlData)){
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[12][8];
            for(int j =0; j<animations.length; j++)
                for(int i = 0; i< animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i*48,j*48,48,48);
    }
    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }
    public void resetDirBooleans(){
        left = false;
        right = false;
        up = false;
        down = false;
    }
    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }

    public void setAnimationTick(int animationTick) {
        this.animationTick = animationTick;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }
}

