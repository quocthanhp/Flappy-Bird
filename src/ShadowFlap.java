import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;


/**
 * SWEN20003 Project 2, Semester 2, 2021, class modified from solution for Project 1 written by Betty Lin
 *
 * @author Quoc Thanh Pham
 */
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE_0 = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/level-1/background.png");
    private final String START_INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String SHOOT_INSTRUCTION_MSG = "PRESS 'S' TO SHOOT";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int LEVEL_UP_FRAME = 150;
    private final int LEVEL_0_THRESHOLD = 10;
    private final int LEVEL_1_THRESHOLD = 30;
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int SHOOT_MSG_OFFSET = 75;
    private final int STANDARD_PIPE_FRAME = 100;
    private final int MIN_TIMESCALE = 1;
    private final int MAX_TIMESCALE = 5;
    private Bird bird;
    private ArrayList<PipeSet> pipeSets;
    private ArrayList<Weapon> weapons;
    private int level;
    private int score;
    private int frameCount;
    private int frameMultiplier;
    private int levelUpFrame;
    private int timescale;
    private double pipeFrame;
    private boolean gameOn;
    private boolean birdPipeCollision;
    private boolean pipeWeaponCollision;
    private boolean weaponReachRange;
    private boolean levelUp;
    private boolean win;
    private boolean reset;


    /** Create Shadow Flap game
     *
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new BirdLevel0();
        pipeSets = new ArrayList<>();
        weapons = new ArrayList<>();
        level = 0;
        score = 0;
        frameCount = 0;
        frameMultiplier = 1;
        levelUpFrame = 0;
        timescale = MIN_TIMESCALE;
        pipeFrame = STANDARD_PIPE_FRAME;
        gameOn = false;
        birdPipeCollision = false;
        pipeWeaponCollision = false;
        weaponReachRange = false;
        levelUp = false;
        win = false;
        reset = false;
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
        if (level == 0 || levelUpFrame < LEVEL_UP_FRAME) {
            BACKGROUND_IMAGE_0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }
        else if (level == 1) {
            if (!reset) {
                resetGameForLevelUp();
                reset = true;
            }
            BACKGROUND_IMAGE_1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        // all bird lives are depleted
        if (bird.getRemainingHearts() == 0) {
            renderGameOverScreen();
        }

        // level up
        if (levelUp) {
            renderLevelUpScreen();
            levelUpFrame++;
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && bird.getRemainingHearts() != 0 && !win && !levelUp) {
            frameCount++;

            bird.update(input);
            bird.updateBirdLifeBar(birdPipeCollision, birdOutOfBound());
            Rectangle birdBox = bird.getBox();

            renderScore();

            adjustTimescale(input);

            if (frameCount % pipeFrame == 0) {
                if (level == 0) {
                    pipeSets.add(new PlasticPipeSet());
                }
                else if (level == 1) {
                    pipeSets.add(randomPipeSet());
                }
            }

            if (level == 1 && frameCount % (pipeFrame * frameMultiplier + pipeFrame / 2.0) == 0) {
                weapons.add(randomWeapon());
                frameMultiplier++;
            }

            for (PipeSet pipeSet : pipeSets) {
                pipeSet.update(level);
                Rectangle topPipeBox = pipeSet.getTopBox();
                Rectangle bottomPipeBox = pipeSet.getBottomBox();

                birdPipeCollision = detectCollision(birdBox, topPipeBox, bottomPipeBox);

                if (birdPipeCollision) {
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

                if (levelUp) {
                    break;
                }

                updateScore(pipeSet);
            }

            for (Weapon weapon : weapons) {
                weapon.update();
                Rectangle weaponBox = weapon.getBoundingBox();

                if (detectPick(birdBox, weaponBox) && !bird.isHoldingWeapon() && !weapon.isPicked()) {
                    bird.holdWeapon(weapon);
                }

                if (input.wasPressed(Keys.S)) {
                    bird.shoot();
                }

                if (weapon.isShot()) {
                    for (PipeSet pipeSet : pipeSets) {
                        Rectangle topPipeBox = pipeSet.getTopBox();
                        Rectangle bottomPipeBox = pipeSet.getBottomBox();

                        pipeWeaponCollision = detectPipeWeaponCollision(weaponBox, topPipeBox, bottomPipeBox);
                        weaponReachRange = weapon.reachRange();

                        if (pipeWeaponCollision) {
                            if (detectDestruction(pipeSet, weapon)) {
                                pipeSet.setDestroyable(true);
                                updateScore(pipeSet);
                                pipeSets.remove(pipeSet);
                                weapons.remove(weapon);
                                break;
                            }
                        }
                        else if (weaponReachRange) {
                            weapons.remove(weapon);
                            break;
                        }
                    }
                }

                if (pipeWeaponCollision || weaponReachRange) {
                    pipeWeaponCollision = false;
                    weaponReachRange = false;
                    break;
                }
            }
        }
    }


    /** Check whether bird is out of bound
     * @return whether bird is out of bound
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }


    /** Draw instruction screen based on level and change game on mode to true if pressed
     * @param input user input
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(START_INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(START_INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));

        if (level == 1) {
            FONT.drawString(SHOOT_INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SHOOT_MSG_OFFSET);
        }

        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }


    /** Draw game over screen
     *
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /** Draw level up screen
     *
     */
    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /** Draw win screen
     *
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /** Check whether bird collides with pipes
     * @param birdBox bird bounding box
     * @param topPipeBox top pipe bounding box
     * @param bottomPipeBox bottom pipe bounding box
     * @return whether bird collides with pipes
     */
    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }


    /** Check whether bird picks weapon
     * @param birdBox bird bounding box
     * @param weaponBox weapon bounding box
     * @return whether bird picks weapon
     */
    public boolean detectPick(Rectangle birdBox, Rectangle weaponBox) {
        return birdBox.intersects(weaponBox);
    }


    /** Check whether weapon collides with pipe
     * @param weaponBox weapon bounding box
     * @param topPipeBox top pipe bounding box
     * @param bottomPipeBox bottom pipe bounding box
     * @return whether weapon collides with pipes
     */
    public boolean detectPipeWeaponCollision(Rectangle weaponBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return weaponBox.intersects(topPipeBox) ||
                weaponBox.intersects(bottomPipeBox);
    }


    /** Check whether weapon destroys pipes
     * @param pipeSet pipe set object
     * @param weapon weapon object
     * @return whether weapon destroys pipes
     */
    public boolean detectDestruction(PipeSet pipeSet, Weapon weapon) {
        if (weapon instanceof Rock && pipeSet instanceof PlasticPipeSet) {
            return true;
        }
        else return weapon instanceof Bomb;
    }


    /** Update score and detect level up or win when threshold is reached
     * @param pipeSet pipe set object
     */
    public void updateScore(PipeSet pipeSet) {
        // bird passes pipes
        if (bird.getX() > pipeSet.getTopBox().right()) {
            if (!pipeSet.ScoreAgainst()) {
                score++;
            }
            pipeSet.setScoreAgainst(true);
        }

        // pipe is destroyed by weapon
        if (pipeSet.isDestroyable()) {
            score++;
        }

        // detect level up
        if (level == 0 && score == LEVEL_0_THRESHOLD) {
            levelUp = true;
            level++;
        }

        // detect win
        if (level == 1 && score == LEVEL_1_THRESHOLD) {
            win = true;
        }
    }


    /** Draw score counter on screen
     *
     */
    public void renderScore() {
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);
    }


    /** Increase timescale by 1 when L is pressed, decrease timescale by 1 when K is pressed
     * @param input user input
     */
    public void adjustTimescale(Input input) {
        if (input.wasPressed(Keys.L) && timescale <= MAX_TIMESCALE) {
            timescale += 1;
            pipeFrame = Math.ceil(pipeFrame / 1.5);
        }
        else if (input.wasPressed(Keys.K) && timescale >= MIN_TIMESCALE) {
            timescale -= 1;
            pipeFrame = Math.ceil(pipeFrame * 1.5);
        }
    }


    /** Reset game for next level
     *
     */
    public void resetGameForLevelUp() {
        gameOn = false;
        birdPipeCollision = false;
        win = false;
        levelUp = false;
        timescale = MIN_TIMESCALE;
        pipeFrame = STANDARD_PIPE_FRAME;
        score = 0;
        frameCount = 0;
        pipeSets = new ArrayList<>();
        bird = new BirdLevel1();
    }


    /** Generate random type of pipe set (steel pipe set, plastic pipe set)
     * @return random type of pipe set
     */
    public PipeSet randomPipeSet() {
        if (Math.random() < 0.5) {
            return new PlasticPipeSet();
        } else {
            return new SteelPipeSet();
        }
    }


    /** Generate random type of weapon (rock, bomb)
     * @return random type of weapon
     */
    public Weapon randomWeapon() {
        if (Math.random() < 0.5) {
            return new Rock();
        } else {
            return new Bomb();
        }
    }
}
