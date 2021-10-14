import bagel.Image;
import bagel.Window;


/** Represent steel pipe set in the game
 *
 */
public class SteelPipeSet extends PipeSet {
    private static final Image STEEL_PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final int SHOOT_FLAME_FRAME = 20;
    private final int FLAME_LAST_FRAME = 30;
    private final int FLAME_HEIGHT = 10;
    private int frameCount = 0;
    private int flameLast = 0;


    /** Create steel pipe set
     *
     */
    public SteelPipeSet() {
        super(STEEL_PIPE_IMAGE);
    }


    /** Draw steel pipe set and flame
     *
     */
    @Override
    public void renderPipeSet() {
        frameCount += 1;
        flameLast += 1;

        super.renderPipeSet();

        if (frameCount % SHOOT_FLAME_FRAME == 0 || flameLast < FLAME_LAST_FRAME) {
            FLAME_IMAGE.draw(getPipeX(), getTopPipeY() + (Window.getHeight()/2.0) + FLAME_HEIGHT);
            FLAME_IMAGE.draw(getPipeX(), getBottomPipeY() - (Window.getHeight()/2.0) - FLAME_HEIGHT, getROTATOR());
            flameLast++;
        }
        else {
            flameLast = 0;
        }
    }
}
