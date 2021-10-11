import bagel.Image;

import java.util.ArrayList;

public class SteelPipeSet extends PipeSet {
    private static final Image STEEL_PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final int SHOOT_FLAME_FRAME = 20;
    private final int FLAME_LAST_FRAME = 3;
    private ArrayList<Flame> flames;
    private Flame f;
    private int frameCount = 0;
    private int flameLast = 0;


    public SteelPipeSet() {
        super(STEEL_PIPE_IMAGE);
        flames = new ArrayList<>();
        f = new Flame();
    }

    @Override
    public void renderPipeSet() {
        frameCount += 1;

        super.renderPipeSet();

        if (frameCount % SHOOT_FLAME_FRAME == 0) {
            //flames.add(new Flame());
            if (flameLast < FLAME_LAST_FRAME) {
                f.renderFlame(pipeX, topPipeY, bottomPipeY, ROTATOR);
                flameLast++;
            } else {
                flameLast = 0;
            }
        }

        /*for (Flame flame : flames) {
           flame.renderFlame(pipeX, topPipeY, bottomPipeY, ROTATOR);
           flameLast++;
           if (flameLast == FLAME_LAST_FRAME) {
                flameLast = 0;
                flames.remove(flame);
                break;
           }
        }*/
    }


}
