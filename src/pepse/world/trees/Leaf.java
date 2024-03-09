package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.main.PepseConstants;
import pepse.main.PepseGameManager;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Represents a leaf object in the game.
 * Leaves change appearance based on the avatar's jumps and have a wind blowing animation.
 *
 * @author adir.barak, asher
 */
class Leaf extends GameObject {
    private static final Color DEFAULT_LEAF_COLOR = new Color(50, 200, 30);
    private static final float WIND_BLOW_TRANSITION_START = 0f;
    private static final float WIND_BLOW_TRANSITION_END = 6f;
    private static final float WIND_BLOW_TRANSITION_DURATION = 1f;
    private static final float WIND_BLOW_TRANSITION_ANGLE_MULTIPLIER = 2f;
    private static final float WIND_BLOW_TRANSITION_X_SIZE_MULTIPLIER = 0.5f;
    private static final boolean WIND_BLOW_SCHEDULE_REPEAT = false;
    private static final Vector2 DEFAULT_LEAF_SIZE = Vector2.ONES.mult(PepseConstants.LEAF_SIZE);
    private final Supplier<Integer> getAvatarJumpCount;
    private final Renderable initialRenderable;
    private int jumpCounter = 0;
    private float jumpTransitionAngle = 0;
    private static final float JUMP_ANGLE_TRANSITION_DELTA = 90f;
    private static final float JUMP_ANGLE_TRANSITION_DURATION = 1f;
    private static final int WHACKY_LUCKY_MAX_RGB = 256;
    private static final double WHACKY_LUCKY_ROLL_NUMBER = 14;

    /**
     * Creates a new Leaf object.
     *
     * @param topLeftCorner        the top left corner of the leaf's position
     * @param countForEventTrigger a function to get the avatar's jump count
     */
    Leaf(Vector2 topLeftCorner, Supplier<Integer> countForEventTrigger) {
        super(topLeftCorner, DEFAULT_LEAF_SIZE,
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_LEAF_COLOR)));
        initialRenderable = renderer().getRenderable();
        this.getAvatarJumpCount = countForEventTrigger;
        GameObject leaf = this;
        float delay = PepseGameManager.random.nextFloat();
        new ScheduledTask(this, delay, WIND_BLOW_SCHEDULE_REPEAT,
                () -> new Transition<Float>(leaf, this::leafTransitionEffects,
                        WIND_BLOW_TRANSITION_START, WIND_BLOW_TRANSITION_END,
                        Transition.LINEAR_INTERPOLATOR_FLOAT, WIND_BLOW_TRANSITION_DURATION,
                        Transition.TransitionType.TRANSITION_LOOP, null));
    }

    /**
     * Disables collision for Leaf objects, as leaves are purely cosmetic.
     * Leaves are decorative and do not participate in collisions.
     *
     * @return always false
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    /**
     * Updates the leaf's appearance based on a transition value (wind blowing effect).
     * This method adjusts the leaf's rotation angle and size based on the provided value.
     *
     * @param value a transition value between 0 and 1
     */
    private void leafTransitionEffects(float value) {
        renderer().setRenderableAngle(value * WIND_BLOW_TRANSITION_ANGLE_MULTIPLIER);
        float newXSize = DEFAULT_LEAF_SIZE.x() - value * WIND_BLOW_TRANSITION_X_SIZE_MULTIPLIER;
        setDimensions(new Vector2(newXSize, DEFAULT_LEAF_SIZE.y()));
    }

    /**
     * Applies a random color to the leaf with a low probability on avatar jumps.
     * This method creates a new renderable with a random RGB color if certain jump count
     * conditions are met. The initial renderable is used otherwise.
     */
    private void rollWhackyLuckyLSDMode() {
        Renderable renderable = initialRenderable;
        if (jumpCounter > 1 && jumpCounter % WHACKY_LUCKY_ROLL_NUMBER == 0) {
            renderable = new RectangleRenderable(new Color(
                    PepseGameManager.random.nextInt(WHACKY_LUCKY_MAX_RGB),
                    PepseGameManager.random.nextInt(WHACKY_LUCKY_MAX_RGB),
                    PepseGameManager.random.nextInt(WHACKY_LUCKY_MAX_RGB)));
        }
        renderer().setRenderable(renderable);

    }

    /**
     * Updates the leaf's state based on the avatar's jump count.
     * If the avatar's jump count has changed, this method triggers a leaf rotation transition
     * and potentially applies a random color with a low probability.
     *
     * @param deltaTime the time elapsed since the last update (in seconds)
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int newJumpCount = getAvatarJumpCount.get();
        if (jumpCounter != newJumpCount) {
            jumpCounter = newJumpCount;
            new Transition<Float>(this, renderer()::setRenderableAngle,
                    jumpTransitionAngle, jumpTransitionAngle + JUMP_ANGLE_TRANSITION_DELTA,
                    Transition.LINEAR_INTERPOLATOR_FLOAT, JUMP_ANGLE_TRANSITION_DURATION,
                    Transition.TransitionType.TRANSITION_ONCE, () -> {
                jumpTransitionAngle += JUMP_ANGLE_TRANSITION_DELTA;
            });
            rollWhackyLuckyLSDMode();
        }
    }
}
