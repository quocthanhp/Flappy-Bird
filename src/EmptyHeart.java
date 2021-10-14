import bagel.Image;


/** Represent empty heart in the game
 *
 */
public class EmptyHeart extends Heart {
    private static final Image EMPTY_HEART_IMAGE = new Image("res/level/noLife.png");


    /** Create empty heart
     *
     */
    public EmptyHeart() {
        super(EMPTY_HEART_IMAGE);
    }
}
