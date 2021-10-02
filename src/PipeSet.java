import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;


public abstract class PipeSet {
    private final Image PIPE_IMAGE = new Image("res/pipe.png");
    private final int PIPE_GAP = 168;
    private final int PIPE_SPEED = 5;
    private final double TOP_PIPE_Y = -PIPE_GAP / 2.0;
    private final double BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();

    public PipeSet() {
    }

    public void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }

    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;
    }

    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }

    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }


}
