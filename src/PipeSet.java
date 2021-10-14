import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/** Base class for all pipe sets in the game. Class modified from solution for Project 1 written by Betty Lin
 *
 */
public abstract class PipeSet {
    private final Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private final int PIPE_SPEED = 5;
    private final int HIGH_GAP_Y = 100;
    private final int MID_GAP_Y = 300;
    private final int LOW_GAP_Y = 500;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double topPipeY;
    private double bottomPipeY;
    private double pipeX = Window.getWidth();
    private boolean firstInstantiated = true;
    private boolean scoreAgainst = false;
    private boolean destroyable = false;


    /** Can not be instantiated
     * @param image pipe set image
     */
    public PipeSet(Image image) {
        PIPE_IMAGE = image;
    }


    /** Draw top pipe and bottom pipe
     *
     */
    protected void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, topPipeY);
        PIPE_IMAGE.draw(pipeX, bottomPipeY, ROTATOR);
    }


    /** Set random y coordinate of centre of pipes from 100, 300, 500
     *
     */
    public void setRandomYLevel0() {
        List<Integer> spawnValue = Arrays.asList(HIGH_GAP_Y, MID_GAP_Y, LOW_GAP_Y);
        Random rand = new Random();
        int randomSpawnValue = spawnValue.get(rand.nextInt(spawnValue.size()));
        topPipeY = - PIPE_GAP / 2.0 + (randomSpawnValue - MID_GAP_Y);
        bottomPipeY = Window.getHeight() + PIPE_GAP  / 2.0 + (randomSpawnValue - MID_GAP_Y);
    }


    /** Set random y coordinate of centre of pipes from [100, 500]
     *
     */
    public void setRandomYLevel1() {
        int randomSpawnValue = LOW_GAP_Y + (int)(Math.random() * ((HIGH_GAP_Y - LOW_GAP_Y) + 1));
        topPipeY = - PIPE_GAP / 2.0 + (randomSpawnValue - MID_GAP_Y);
        bottomPipeY = Window.getHeight() + PIPE_GAP  / 2.0 + (randomSpawnValue - MID_GAP_Y);
    }


    /** Update state of pipes
     * @param level level number
     */
    public void update(int level) {
        if (firstInstantiated) {
            if (level == 0) {
                setRandomYLevel0();
                firstInstantiated = false;
            }
            else if (level == 1) {
                setRandomYLevel1();
                firstInstantiated = false;
            }
        }
        renderPipeSet();
        pipeX -= PIPE_SPEED;
    }


    /** Get y coordinate of centre of top pipe
     * @return y coordinate of centre of top pipe
     */
    public double getTopPipeY() {
        return topPipeY;
    }


    /** Get y coordinate of centre of bottom pipe
     * @return y coordinate of centre of bottom pipe
     */
    public double getBottomPipeY() {
        return bottomPipeY;
    }


    /** Get x coordinate of centre of pipe
     * @return x coordinate of centre of pipe
     */
    public double getPipeX() {
        return pipeX;
    }


    /** Get DrawOptions applied on bottom pipe
     * @return DrawOptions applied on bottom pipe
     */
    public DrawOptions getROTATOR() {
        return ROTATOR;
    }


    /** Get bounding box of top pipe
     * @return bounding box of top pipe
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, topPipeY));
    }


    /** Get bounding box of bottom pipe
     * @return bounding box of bottom pipe
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, bottomPipeY));
    }


    /** Check whether pipe set is scored against by bird
     * @return whether pipe set is scored against by bird
     */
    public boolean ScoreAgainst() {
        return scoreAgainst;
    }


    /** Set scoreAgainst state of pipes to true/false
     * @param scoreAgainst scoreAgainst state
     */
    public void setScoreAgainst(boolean scoreAgainst) {
        this.scoreAgainst = scoreAgainst;
    }


    /** Check whether pipe set is destroyed by weapon shot by bird
     * @return whether pipe set is destroyed by weapon shot by bird
     */
    public boolean isDestroyable() {
        return destroyable;
    }


    /** Set destroyable state of pipes to true/false
     * @param destroyable destroyable state
     */
    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

}
