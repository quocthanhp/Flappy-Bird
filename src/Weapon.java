import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Weapon {
    private final Image WEAPON_IMAGE;
    private final int SHOOTING_RANGE;
    private final int MAX_Y = 500;
    private final int MIN_Y = 100;
    private final int TRAVEL_SPEED = 5;
    private double weaponX = Window.getWidth();
    private double weaponY;
    private Rectangle boundingBox;
    private boolean firstInstantiated = true;
    private boolean shot = false;


    public Weapon(Image image, int shootingRange) {
        this.WEAPON_IMAGE = image;
        this.SHOOTING_RANGE = shootingRange;
        boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    public void setRandomY() {
        weaponY = MIN_Y + (int)(Math.random() * ((MAX_Y - MIN_Y) + 1));
    }

    public void renderWeapon() {
        WEAPON_IMAGE.draw(weaponX, weaponY);
    }

    public void attachWeapon(double x, double y) {
        WEAPON_IMAGE.draw(x, y);
    }

    public void update() {
        if (firstInstantiated) {
            setRandomY();
            firstInstantiated = false;
        }
        renderWeapon();
        boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        if (!shot) {
            weaponX -= TRAVEL_SPEED;
        } else {
            weaponX += TRAVEL_SPEED;
        }
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}

