package entities;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.geom.Rectangle2D;
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

    //Status Bar UI

    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    //AttackBox
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x,y,(int)(20 * Game.SCALE),(int)(29.5 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(20* Game.SCALE),(int)(20 * Game.SCALE));
    }

    public void update(){
        updateHealthBar();
        if(currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();

        updatePosition();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || animationIndex !=1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE);
        }
        attackBox.y = hitbox.y + (Game.SCALE);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y - yDrawOffset), width * flipW,height, null);
//        drawHitbox(g, xLvlOffset);
        drawAttackBox(g,lvlOffset);

        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth,healthBarHeight);
    }

    private void updateAnimationTick() {

        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(playerAction)){
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
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
            if(startAnimation != ATTACK_2){
                animationIndex = 1;
                animationTick = 0;
                return;
            }
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

        if(left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if(right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
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
    public void changeHealth (int value){
        currentHealth += value;
        if(currentHealth <= 0){
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[12][8];
            for(int j =0; j<animations.length; j++)
                for(int i = 0; i< animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i*48,j*48,48,48);

            statusBarImg = LoadSave.GetPlayerAtlas(LoadSave.STATUS_BAR);
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
    public void resetAll(){
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }
}

