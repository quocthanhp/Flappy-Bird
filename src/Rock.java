import bagel.Image;


/** Represent rock weapon in the game
 *
 */
public class Rock extends Weapon {
    private static final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private static final int RANGE = 25;


    /** Create rock
     *
     */
    public Rock() {
        super(ROCK_IMAGE, RANGE);
    }

}
