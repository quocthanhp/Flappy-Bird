import bagel.Image;


/** Represent bomb weapon in the game
 *
 */
public class Bomb extends Weapon {
    private static final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private static final int RANGE = 50;


    /** Create bomb
     *
     */
    public Bomb() {
        super(BOMB_IMAGE, RANGE);
    }
}
