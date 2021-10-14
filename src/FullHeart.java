import bagel.Image;


/** Represent full heart in the game
 *
 */
public class FullHeart extends Heart {
    private static final Image FULL_HEART_IMAGE = new Image("res/level/fullLife.png");


    /** Create full heart
     *
     */
    public FullHeart() {
        super(FULL_HEART_IMAGE);
    }
}
