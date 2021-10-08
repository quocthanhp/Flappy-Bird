import bagel.*;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 2, 2021
 *
 * @author Betty Lin
 */
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE_0 = new Image("res/level-0/background.png");
    private final String START_INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int LEVEL_0_THRESHOLD = 10;
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int STANDARD_PIPE_FRAME = 100;
    private final int MIN_TIMESCALE = 1;
    private final int MAX_TIMESCALE = 5;
    private Bird bird;
    private ArrayList<PlasticPipeSet> pipeSets;
    private int level;
    private int score;
    private int frameCount;
    private int timescale;
    private double pipeFrame;
    private boolean gameOn;
    private boolean collision;
    private boolean win;

    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new BirdLevel0();
        pipeSets = new ArrayList<>();
        level = 0;
        score = 0;
        frameCount = 0;
        timescale = 1;
        pipeFrame = STANDARD_PIPE_FRAME;
        gameOn = false;
        collision = false;
        win = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        frameCount++;


        BACKGROUND_IMAGE_0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        // all bird lives are depleted
        if (bird.getRemainingLives() == 0) {
            renderGameOverScreen();
        }

        // game won
        if (win) {
            renderLevelUpScreen();
        }


        // game is active
        if (gameOn && bird.getRemainingLives() != 0 && !win) {

            bird.update(input);
            bird.updateBirdLife(collision, birdOutOfBound());
            Rectangle birdBox = bird.getBox();

            renderScore();

            adjustTimescale(input);

            if (frameCount%pipeFrame == 0) {
                pipeSets.add(new PlasticPipeSet());
            }

            for (PlasticPipeSet pipeSet : pipeSets) {

                pipeSet.update();

                Rectangle topPipeBox = pipeSet.getTopBox();
                Rectangle bottomPipeBox = pipeSet.getBottomBox();

                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);

                if (collision) {
                    pipeSets.remove(pipeSet);
                    break;
                }

                if (birdOutOfBound()) {
                    bird.reset();
                    break;
                }

                if (win) {
                    break;
                }

                updateScore(pipeSet);

            }

        }
    }

    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(START_INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(START_INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    public void updateScore(PlasticPipeSet pipeSet) {
        if (bird.getX() > pipeSet.getTopBox().right()) {
            if (!pipeSet.ScoreAgainst()) {
                score++;
            }
            pipeSet.setScoreAgainst(true);
        }

        // detect level up
        if (score == LEVEL_0_THRESHOLD) {
            win = true;
            level++;
        }
    }

    void renderScore() {
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);
    }

    void adjustTimescale(Input input) {
        if (input.wasPressed(Keys.L) && timescale <= MAX_TIMESCALE) {
            timescale+=1;
            pipeFrame = Math.ceil(pipeFrame / 1.5);
        } else if (input.wasPressed(Keys.K) && timescale >= MIN_TIMESCALE) {
            timescale-=1;
            pipeFrame = Math.ceil(pipeFrame * 1.5);
        }
    }

}
