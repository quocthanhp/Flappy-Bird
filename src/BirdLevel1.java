import bagel.Image;


/** Represent bird at level 0 in the game
 *
 */
public class BirdLevel1 extends Bird {
    private static final Image WING_DOWN = new Image("res/level-1/birdWingDown.png");
    private static final Image WING_UP = new Image("res/level-1/birdWingUp.png");
    private static final int MAX_LIVES = 6;


    /** Create bird at level 1
     *
     */
    public BirdLevel1() {
        super(WING_DOWN, WING_UP, MAX_LIVES);
    }

}
