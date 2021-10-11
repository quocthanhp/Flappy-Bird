import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;

public class Flame {
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final double FLAME_PIXEL = 10;

    public void renderFlame(double pipeX, double topPipeY, double bottomPipeY, DrawOptions rotator) {
        FLAME_IMAGE.draw(pipeX, topPipeY + (Window.getHeight()/2.0) + FLAME_PIXEL);
        FLAME_IMAGE.draw(pipeX, bottomPipeY - (Window.getHeight()/2.0) - FLAME_PIXEL, rotator);
    }
}
