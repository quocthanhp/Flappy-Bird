import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

public class Bird {
    private final Image WING_DOWN_IMAGE;
    private final Image WING_UP_IMAGE;
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double y = INITIAL_Y;
    private double yVelocity = 0;
    private Rectangle boundingBox;

    public Bird(Image wingDown, Image wingUp) {
        WING_DOWN_IMAGE = wingDown;
        WING_UP_IMAGE = wingUp;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
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
}