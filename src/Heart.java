import bagel.Image;


/** Base class for all hearts in the game
 *
 */
public abstract class Heart {
    private final Image HEART_IMAGE;


    /** Can not be instantiated
     * @param image heart image
     */
    public Heart(Image image) {
        this.HEART_IMAGE = image;
    }


    /** Draw heart with its top left at (x, y)
     * @param x x coordinate
     * @param y y coordinate
     */
    public void renderHeart(double x, double y) {
        HEART_IMAGE.drawFromTopLeft(x, y);
    }

}
