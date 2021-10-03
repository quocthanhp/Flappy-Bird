import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public abstract class PipeSet {
    private final Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private final int PIPE_SPEED = 5;
    private final int SPAWNED_FRAME = 100;
    private final int HIGH_GAP_Y = 100;
    private final int MID_GAP_Y = 300;
    private final int LOW_GAP_Y = 500;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double topPipeY;
    private double bottomPipeY;
    private double pipeX = Window.getWidth();


    public PipeSet(Image image) {
        PIPE_IMAGE = image;
    }

    protected void renderPipeSet() {

        PIPE_IMAGE.draw(pipeX, topPipeY);
        PIPE_IMAGE.draw(pipeX, bottomPipeY, ROTATOR);
    }

    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;
    }

    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, topPipeY));

    }

    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, bottomPipeY));

    }


}
