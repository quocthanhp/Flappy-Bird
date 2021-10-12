import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;

public class BirdLevel1 extends Bird {
    private static final Image WING_DOWN = new Image("res/level-1/birdWingDown.png");
    private static final Image WING_UP = new Image("res/level-1/birdWingUp.png");
    private static final int MAX_LIVES = 6;
    private Weapon weapon;

    public BirdLevel1() {
        super(WING_DOWN, WING_UP, MAX_LIVES);
    }

    /* public void pickWeapon(Weapon weapon) {
        this.weapon = weapon;
        weapon.attachWeapon(getBox().bottomRight().x, getBox().bottomRight().y);
    }

    public void shoot(Input input) {
        if (input.wasPressed(Keys.S)) {
            weapon.update();
        }
    }*/

}
