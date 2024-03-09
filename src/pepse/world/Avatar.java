package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.main.PepseConstants;

import java.awt.event.KeyEvent;

/**
 * Represents the player character (avatar) in the game.
 *
 * @author adir.barak, asher
 */
public class Avatar extends GameObject {

    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -600;
    private static final float GRAVITY = 800;
    private static final double IDLE_FRAME_RATE = 0.2;
    private static final boolean IDLE_TRANSPARENCY_FLAG = true;
    private static final double MOVING_FRAME_RATE = 0.01;
    private static final boolean MOVING_TRANSPARENCY_FLAG = true;
    private static final double AIRBORNE_FRAME_RATE = 0.4;
    private static final boolean AIRBORNE_TRANSPARENCY_FLAG = true;
    private final UserInputListener inputListener;

    private static final float MAX_ENERGY_LEVEL = 100f;
    private static final float MIN_ENERGY_LEVEL = 0f;
    private float currentEnergyLevel;
    private static final float MOVE_ENERGY_COST = -0.5f;
    private static final float JUMP_ENERGY_COST = -10f;
    private static final float IDLE_ENERGY_GAIN = 1f;
    private boolean isAirborne = false;
    private boolean isMoving = false;

    private int jumpCounter = 0;

    private static final String[] IDLE_ANIMATION_FRAMES = new String[]{"./assets/idle_0.png",
            "./assets/idle_1.png", "./assets/idle_2.png", "./assets/idle_3.png"};
    private static final String[] MOVING_ANIMATION_FRAMES = new String[]{"./assets/run_0.png",
            "./assets/run_1.png", "./assets/run_2.png", "./assets/run_3.png", "./assets/run_4.png",
            "./assets/run_5.png"};
    private static final String[] AIRBORNE_ANIMATION_FRAMES = new String[]{"./assets/jump_0.png",
            "./assets/jump_1.png", "./assets/jump_2.png", "./assets/jump_3.png"};
    private AnimationRenderable idleAnimation;
    private AnimationRenderable movingAnimation;
    private AnimationRenderable airborneAnimation;

    private static final Vector2 DEFAULT_SIZE = new Vector2(
            PepseConstants.BLOCK_SIZE, 2 * PepseConstants.BLOCK_SIZE);


    /**
     * Constructs an Avatar object with the specified position, input listener, and image reader.
     *
     * @param pos           The initial position of the avatar.
     * @param inputListener The input listener for controlling the avatar.
     * @param imageReader   The image reader for loading avatar sprites.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, new Vector2(DEFAULT_SIZE), null);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        currentEnergyLevel = MAX_ENERGY_LEVEL;
        initAnimationRenderables(imageReader);
        setTag(PepseConstants.AVATAR_TAG);
    }

    /**
     * Initializes the animation renderables for the character.
     *
     * @param imageReader the ImageReader object that provides access to the character's animation frames
     */
    private void initAnimationRenderables(ImageReader imageReader) {
        idleAnimation = new AnimationRenderable(IDLE_ANIMATION_FRAMES, imageReader,
                IDLE_TRANSPARENCY_FLAG, IDLE_FRAME_RATE);
        movingAnimation = new AnimationRenderable(MOVING_ANIMATION_FRAMES, imageReader,
                MOVING_TRANSPARENCY_FLAG, MOVING_FRAME_RATE);
        airborneAnimation = new AnimationRenderable(AIRBORNE_ANIMATION_FRAMES, imageReader,
                AIRBORNE_TRANSPARENCY_FLAG, AIRBORNE_FRAME_RATE);
    }

    /**
     * Positions the character on the ground at a specified height.
     *
     * @param xCoordinate    the x-coordinate of the desired position
     * @param platformHeight the height of the platform the character should stand on
     */
    public void positionOnGroundAtHeight(float xCoordinate, float platformHeight) {
        setTopLeftCorner(new Vector2(xCoordinate, platformHeight - getDimensions().y()));
    }

    /**
     * Calculates the character's current energy level as a percentage.
     *
     * @return the character's current energy level as a percentage (0f to 100f)
     */
    public float getCurrentEnergyLevelPercentage() {
        return (currentEnergyLevel / MAX_ENERGY_LEVEL) * 100f;
    }

    /**
     * Changes the character's energy level by a specified value.
     *
     * @param value the amount to change the energy level by (positive or negative)
     */
    public void changeEnergyBy(float value) {
        currentEnergyLevel = Math.min(MAX_ENERGY_LEVEL, currentEnergyLevel + value);
        currentEnergyLevel = Math.max(MIN_ENERGY_LEVEL, currentEnergyLevel);
    }

    /**
     * Gets the character's current jump counter.
     *
     * @return the character's current jump counter
     */
    public int getJumpCounter() {
        return jumpCounter;
    }

    /**
     * Updates the character's state based on input and game logic.
     *
     * @param deltaTime the time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderer().setRenderable(idleAnimation);
        handleRun();
        handleJump();
        if (!isMoving && !isAirborne) {
            changeEnergyBy(IDLE_ENERGY_GAIN);
        }
        if (isMoving) {
            renderer().setRenderable(movingAnimation);
        } else if (isAirborne) {
            renderer().setRenderable(airborneAnimation);
        }
    }

    /**
     * Handles the character's jumping logic.
     */
    private void handleJump() {
        isAirborne = getVelocity().y() != 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && !isAirborne) {
            if (currentEnergyLevel + JUMP_ENERGY_COST >= MIN_ENERGY_LEVEL) {
                isAirborne = true;
                jumpCounter++;
                transform().setVelocityY(VELOCITY_Y);
                changeEnergyBy(JUMP_ENERGY_COST);
            }
        }
    }

    /**
     * Handles the character's running logic.
     */
    private void handleRun() {
        isMoving = false;
        transform().setVelocityX(0);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        if (xVel != 0) {
            // meaning the avatar tries to move
            if (currentEnergyLevel + MOVE_ENERGY_COST >= MIN_ENERGY_LEVEL) {
                renderer().setIsFlippedHorizontally(xVel < 0);
                isMoving = true;
                changeEnergyBy(MOVE_ENERGY_COST);
                transform().setVelocityX(xVel);
            }
        }
    }
}
