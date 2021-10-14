import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;


/** Base class for all weapons in the game
 *
 */
public abstract class Weapon {
    private final Image WEAPON_IMAGE;
    private final int SHOOTING_RANGE;
    private final int MAX_Y = 500;
    private final int MIN_Y = 100;
    private final int MIN_TIMESCALE = 1;
    private final int MAX_TIMESCALE = 5;
    private int timescale = MIN_TIMESCALE;
    private double weaponX = Window.getWidth();
    private double weaponY;
    private double xVelocity;
    private double weaponSpeed = 5;
    private double weaponShootingSpeed = 5;
    private Rectangle boundingBox;
    private boolean firstInstantiated = true;
    private boolean shot = false;
    private boolean reachRange = false;
    private boolean picked = false;


    /** Can not be instantiated
     * @param image weapon image
     * @param shootingRange weapon shooting range
     */
    public Weapon(Image image, int shootingRange) {
        this.WEAPON_IMAGE = image;
        this.SHOOTING_RANGE = shootingRange;
        boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
    }


    /** Set random y coordinate of centre of weapon from range [100, 500]
     *
     */
    public void setRandomY() {
        weaponY = MIN_Y + (int)(Math.random() * ((MAX_Y - MIN_Y) + 1));
    }


    /** Draw weapon based on its centre represented by x, y coordinates
     *
     */
    public void renderWeapon() {
        WEAPON_IMAGE.draw(weaponX, weaponY);
    }


    /** Update state of weapon
     * @param input user input
     */
    public void update(Input input) {
        adjustTimescaleForWeaponSpeed(input);

        if (firstInstantiated) {
            setRandomY();
            firstInstantiated = false;
        }

        if (!picked) {
            renderWeapon();
            weaponX -= weaponSpeed;
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }

        if (picked && !shot) {
            renderWeapon();
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }

        if (shot) {
            if (xVelocity < SHOOTING_RANGE) {
                xVelocity += weaponShootingSpeed;
                weaponX += xVelocity;
                renderWeapon();
            } else {
                reachRange = true;
            }
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }
    }


    /** Increase timescale for weapon speed by 1 when L is pressed, decrease timescale for weapon speed by 1 when K is pressed
     * @param input user input
     */
    public void adjustTimescaleForWeaponSpeed(Input input) {
        if (input.wasPressed(Keys.L) && timescale <= MAX_TIMESCALE) {
            timescale += 1;
            weaponSpeed = Math.ceil(weaponSpeed * 1.5);
        }
        else if (input.wasPressed(Keys.K) && timescale >= MIN_TIMESCALE) {
            timescale -= 1;
            weaponSpeed = Math.ceil(weaponSpeed / 1.5);
        }
    }


    /** Get bounding box of weapon
     * @return bounding box of weapon
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }


    /** Set x coordinates of centre of weapon to given value
     * @param weaponX x value
     */
    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }


    /** Set y coordinates of centre of weapon to given value
     * @param weaponY y value
     */
    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }


    /** Set weapon speed to given value
     * @param weaponSpeed
     */
    public void setWeaponSpeed(double weaponSpeed) {
        this.weaponSpeed = weaponSpeed;
    }


    /** Set shooting state of weapon to true/false
     * @param shot shot state
     */
    public void setShot(boolean shot) {
        this.shot = shot;
    }


    /** Check whether weapon is shot by bird
     * @return whether weapon is shot by bird
     */
    public boolean isShot() {
        return shot;
    }


    /** Set picked state of weapon to true/false
     * @param picked picked state
     */
    public void setPicked(boolean picked) {
        this.picked = picked;
    }


    /** Check whether weapon is picked
     * @return whether weapon is picked
     */
    public boolean isPicked() {
        return picked;
    }


    /** Check whether weapon reaches its shooting range
     * @return whether weapon reaches its shooting range
     */
    public boolean reachRange() {
        return reachRange;
    }
}

