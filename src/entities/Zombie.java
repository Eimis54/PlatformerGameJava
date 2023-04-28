package entities;
import main.Game;

import static utils.Constants.EnemyConstants.*;
public class Zombie extends Enemy {
    public Zombie(float x, float y) {
        super(x, y, ZOMBIE_WIDTH, ZOMBIE_HEIGHT, ZOMBIE);
        initHitbox(x,y,(int)(22* Game.SCALE),(int)(28*Game.SCALE));


    }
}
