import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
import java.util.ArrayList;

public class Bird {
    private final Image WING_DOWN_IMAGE;
    private final Image WING_UP_IMAGE;
    private final ArrayList<Heart> lifeBar;
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private final int MAX_LIVES;
    private final int SPACES = 50;
    private int remainingLives;
    private int spacesCount = 0;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private boolean start;

    public Bird(Image wingDown, Image wingUp, int maxLivesNum) {
        WING_DOWN_IMAGE = wingDown;
        WING_UP_IMAGE = wingUp;
        lifeBar = new ArrayList<>();
        MAX_LIVES = maxLivesNum;
        remainingLives = maxLivesNum;
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
        start = true;
    }

    public void renderBirdLifeBar() {

        for (int i = 0; i < MAX_LIVES; i++) {
            lifeBar.get(i).renderHeart(spacesCount);
            spacesCount += SPACES;
        }
        spacesCount = 0;
    }

    public void updateBirdLife(boolean collision, boolean outOfBound) {
        if (start) {
            for (int i = 0; i < MAX_LIVES; i++) {
                lifeBar.add(new FullHeart());
            }
            start = false;
        }

        if (collision || outOfBound) {
            lifeBar.remove(remainingLives-1);
            lifeBar.add(new EmptyHeart());
            remainingLives--;
        }

        renderBirdLifeBar();
    }

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

        return boundingBox;
    }
    public double getY() {
        return y;
    }

    public double getX() {
        return X;
    }

    public Rectangle getBox() {
        return boundingBox;
    }

    public int getRemainingLives() {
        return remainingLives;
    }
}