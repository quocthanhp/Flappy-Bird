import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class BirdLevel0 extends Bird {
    private static final Image WING_DOWN = new Image("res/level-0/birdWingDown.png");
    private static final Image WING_UP = new Image("res/level-0/birdWingUp.png");
    private static final int MAX_LIVES = 3;

    public BirdLevel0() {
        super(WING_DOWN, WING_UP, MAX_LIVES);
    }




}
