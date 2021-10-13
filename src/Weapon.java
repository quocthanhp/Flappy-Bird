import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Weapon {
    private final Image WEAPON_IMAGE;
    private final int SHOOTING_RANGE;
    private final int MAX_Y = 500;
    private final int MIN_Y = 100;
    private final int TRAVEL_SPEED = 5;
    private double weaponX = Window.getWidth();
    private double weaponY;
    private double xVelocity;
    private Rectangle boundingBox;
    private boolean firstInstantiated = true;
    private boolean shot = false;
    private boolean reachRange = false;
    private boolean picked = false;


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


    public void update() {

        if (firstInstantiated) {
            setRandomY();
            firstInstantiated = false;
        }

        if (!picked) {
            renderWeapon();
            weaponX -= TRAVEL_SPEED;
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }

        if (picked && !shot) {
            renderWeapon();
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }

        if (shot) {
            if (xVelocity < SHOOTING_RANGE) {
                xVelocity += TRAVEL_SPEED;
                weaponX += xVelocity;
                renderWeapon();
            } else {
                reachRange = true;
            }
            boundingBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        }
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public boolean isShot() {
        return shot;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isPicked() {
        return picked;
    }

    public boolean reachRange() {
        return reachRange;
    }
}

