import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
import java.util.ArrayList;


/** Base class for all birds in the game. Class modified from solution for Project 1 written by Betty Lin
 *
 */
public abstract class Bird {
    private final Image WING_DOWN_IMAGE;
    private final Image WING_UP_IMAGE;
    private final ArrayList<Heart> lifeBar;
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private final double INITIAL_HEART_X = 100;
    private final double INITIAL_HEART_Y = 15;
    private final double HEART_SPACES = 50;
    private final int MAX_LIVES;
    private int remainingHearts;
    private int spacesCount = 0;
    private int frameCount = 0;
    private double y = INITIAL_Y;
    private double yVelocity = 0;
    private boolean firstInstantiated = true;
    private boolean holdingWeapon = false;
    private Rectangle boundingBox;
    private Weapon weapon;


    /** Can not be instantiated
     * @param wingDown bird wing down image
     * @param wingUp bird wing up image
     * @param maxHeartNum max number of hearts
     */
    public Bird(Image wingDown, Image wingUp, int maxHeartNum) {
        WING_DOWN_IMAGE = wingDown;
        WING_UP_IMAGE = wingUp;
        lifeBar = new ArrayList<>();
        MAX_LIVES = maxHeartNum;
        remainingHearts = maxHeartNum;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
    }


    /** Draw life bar of the bird
     *
     */
    public void renderBirdLifeBar() {
        for (int i = 0; i < MAX_LIVES; i++) {
            lifeBar.get(i).renderHeart(INITIAL_HEART_X + spacesCount, INITIAL_HEART_Y);
            spacesCount += HEART_SPACES;
        }
        spacesCount = 0;
    }


    /** Update life bar of bird by removing one heart in case of collision or out of bound
     * @param collision collision state
     * @param outOfBound out of bound state
     */
    public void updateBirdLifeBar(boolean collision, boolean outOfBound) {
        if (firstInstantiated) {
            for (int i = 0; i < MAX_LIVES; i++) {
                lifeBar.add(new FullHeart());
            }
            firstInstantiated = false;
        }

        if (collision || outOfBound) {
            lifeBar.remove(remainingHearts-1);
            lifeBar.add(new EmptyHeart());
            remainingHearts--;
        }
        renderBirdLifeBar();
    }


    /** Update state of bird when flying
     * @param input user input
     * @return bounding box of the bird
     */
    public Rectangle update(Input input) {
        frameCount += 1;

        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            WING_DOWN_IMAGE.draw(X, y);
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                WING_UP_IMAGE.draw(X, y);
                boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
            else {
                WING_DOWN_IMAGE.draw(X, y);
                boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        if (holdingWeapon && !weapon.isShot() && weapon.isPicked()) {
            weapon.setWeaponX(boundingBox.bottomRight().x);
            weapon.setWeaponY(boundingBox.bottomRight().y);
        }
        return boundingBox;
    }


    /** Instantiate weapon picked by bird
     * @param pickedWeapon weapon picked by bird
     */
    public void holdWeapon(Weapon pickedWeapon) {
        if (!holdingWeapon) {
            weapon = pickedWeapon;
            weapon.setPicked(true);
            holdingWeapon = true;
        }
    }


    /** Update the state of bird and its weapon when bird shoots weapon
     *
     */
    public void shoot() {
        if (holdingWeapon) {
            weapon.setShot(true);
            holdingWeapon = false;
        }
    }


    /** Reset position of bird in case of out of bound
     *
     */
    public void reset() {
        y = INITIAL_Y;
    }


    /** Get y coordinates of centre of bird
     * @return y coordinates of centre of bird
     */
    public double getY() {
        return y;
    }


    /** Get x coordinates of centre of bird
     * @return x coordinates of centre of bird
     */
    public double getX() {
        return X;
    }


    /** Get bounding box of bird
     * @return bounding box of bird
     */
    public Rectangle getBox() {
        return boundingBox;
    }


    /** Get number of remaining hearts of bird
     * @return number of remaining hearts of bird
     */
    public int getRemainingHearts() {
        return remainingHearts;
    }


    /** Check whether bird is holding weapon
     * @return whether bird is holding weapon
     */
    public boolean isHoldingWeapon() {
        return holdingWeapon;
    }
}