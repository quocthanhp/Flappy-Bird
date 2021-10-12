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
    private final Image BACKGROUND_IMAGE_1 = new Image("res/level-1/background.png");
    private final String START_INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String SHOOT_INSTRUCTION_MSG = "PRESS 'S' TO SHOOT";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int LEVEL_UP_FRAME = 150;
    private final int LEVEL_0_THRESHOLD = 1;
    private final int LEVEL_1_THRESHOLD = 20;
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int SHOOT_MSG_OFFSET = 75;
    private final int STANDARD_PIPE_FRAME = 100;
    private final int STANDARD_WEAPON_FRAME = 150;
    private final int MIN_TIMESCALE = 1;
    private final int MAX_TIMESCALE = 5;
    private Bird[] bird;
    private ArrayList<PipeSet> pipeSets;
    private ArrayList<Weapon> weapons;
    private int level;
    private int score;
    private int frameCount;
    private int levelUpFrame;
    private int timescale;
    private double pipeFrame;
    private double weaponFrame;
    private boolean gameOn;
    private boolean collision;
    private boolean levelUp;
    private boolean win;
    private boolean reset;

    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird[] {new BirdLevel0(), new BirdLevel1()};
        pipeSets = new ArrayList<>();
        weapons = new ArrayList<>();
        level = 0;
        score = 0;
        frameCount = 0;
        levelUpFrame = 0;
        timescale = MIN_TIMESCALE;
        pipeFrame = STANDARD_PIPE_FRAME;
        weaponFrame = STANDARD_WEAPON_FRAME;
        gameOn = false;
        collision = false;
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
                resetGame();
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
        if (bird[level].getRemainingLives() == 0) {
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
        if (gameOn && bird[level].getRemainingLives() != 0 && !win && !levelUp) {
            frameCount++;

            bird[level].update(input);
            bird[level].updateBirdLife(collision, birdOutOfBound());
            Rectangle birdBox = bird[level].getBox();

            renderScore();

            adjustTimescale(input);

            if (level==0) {
                if (frameCount % pipeFrame == 0) {
                    pipeSets.add(new PlasticPipeSet());
                }
            }

            if (level == 1) {
                if (frameCount % pipeFrame == 0) {
                    pipeSets.add(randomPipeSet());
                }

                if (frameCount % weaponFrame == 0) {
                    weapons.add(randomWeapon());
                }

                for (Weapon weapon : weapons) {
                    weapon.update();

                    Rectangle weaponBox = weapon.getBoundingBox();

                    if (detectPick(birdBox, weaponBox)) {
                        bird[level].holdWeapon(weapon);
                        bird[level].shoot(input);
                    }

                    if (weapon.reachRange()) {
                        weapons.remove(weapon);
                        break;
                    }

                }

            }

            for (PipeSet pipeSet : pipeSets) {
                pipeSet.update(level);

                Rectangle topPipeBox = pipeSet.getTopBox();
                Rectangle bottomPipeBox = pipeSet.getBottomBox();

                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);

                if (collision) {
                    pipeSets.remove(pipeSet);
                    break;
                }

                if (birdOutOfBound()) {
                    bird[level].reset();
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

        }
    }

    public boolean birdOutOfBound() {
        return (bird[level].getY() > Window.getHeight()) || (bird[level].getY() < 0);
    }

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

    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    public boolean detectPick(Rectangle birdBox, Rectangle weapon) {
        // check for pick
        return birdBox.intersects(weapon);

    }

    public void updateScore(PipeSet pipeSet) {
        if (bird[level].getX() > pipeSet.getTopBox().right()) {
            if (!pipeSet.ScoreAgainst()) {
                score++;
            }
            pipeSet.setScoreAgainst(true);
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

    public void renderScore() {
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);
    }

    public void adjustTimescale(Input input) {
        if (input.wasPressed(Keys.L) && timescale <= MAX_TIMESCALE) {
            timescale+=1;
            pipeFrame = Math.ceil(pipeFrame / 1.5);
            if (level == 1) {
                weaponFrame = Math.ceil(pipeFrame / 1.5);
            }
        } else if (input.wasPressed(Keys.K) && timescale >= MIN_TIMESCALE) {
            timescale-=1;
            pipeFrame = Math.ceil(pipeFrame * 1.5);
            if (level == 1) {
                weaponFrame = Math.ceil(pipeFrame * 1.5);
            }
        }
    }

    public void resetGame() {
        gameOn = false;
        collision = false;
        win = false;
        levelUp = false;
        timescale = MIN_TIMESCALE;
        pipeFrame = STANDARD_PIPE_FRAME;
        score = 0;
        frameCount = 0;
        pipeSets = new ArrayList<>();
    }

    public PipeSet randomPipeSet() {
        if (Math.random() < 0.5) {
            return new PlasticPipeSet();
        } else {
            return new SteelPipeSet();
        }
    }

    public Weapon randomWeapon() {
        if (Math.random() < 0.5) {
            return new Rock();
        } else {
            return new Bomb();
        }
    }

}
