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
    private final int HIGH_GAP_Y = 100;
    private final int MID_GAP_Y = 300;
    private final int LOW_GAP_Y = 500;
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    protected double topPipeY;
    protected double bottomPipeY;
    protected double pipeX = Window.getWidth();
    private boolean firstInstantiated;
    private boolean scoreAgainst;


    public PipeSet(Image image) {
        PIPE_IMAGE = image;
        firstInstantiated = true;
        scoreAgainst = false;
    }

    protected void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, topPipeY);
        PIPE_IMAGE.draw(pipeX, bottomPipeY, ROTATOR);
    }

    public void setRandomY0() {
        List<Integer> spawnValue = Arrays.asList(HIGH_GAP_Y, MID_GAP_Y, LOW_GAP_Y);
        Random rand = new Random();
        int randomSpawnValue = spawnValue.get(rand.nextInt(spawnValue.size()));

        topPipeY = - PIPE_GAP / 2.0 + (randomSpawnValue - MID_GAP_Y);
        bottomPipeY = Window.getHeight() + PIPE_GAP  / 2.0 + (randomSpawnValue - MID_GAP_Y);
    }

    public void setRandomY1() {
        int randomSpawnValue = LOW_GAP_Y + (int)(Math.random() * ((HIGH_GAP_Y - LOW_GAP_Y) + 1));

        topPipeY = - PIPE_GAP / 2.0 + (randomSpawnValue - MID_GAP_Y);
        bottomPipeY = Window.getHeight() + PIPE_GAP  / 2.0 + (randomSpawnValue - MID_GAP_Y);
    }


    public void update(int level) {
        if (firstInstantiated) {
            if (level == 0) {
                setRandomY0();
                firstInstantiated = false;
            } else if (level == 1) {
                setRandomY1();
                firstInstantiated = false;
            }
        }
        renderPipeSet();
        pipeX -= PIPE_SPEED;
    }

    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, topPipeY));
    }

    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, bottomPipeY));
    }

    public double getPipeX() {
        return pipeX;
    }

    public boolean ScoreAgainst() {
        return scoreAgainst;
    }

    public void setScoreAgainst(boolean scoreAgainst) {
        this.scoreAgainst = scoreAgainst;
    }
}
