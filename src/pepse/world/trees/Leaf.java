package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

public class Leaf extends GameObject {
    private static final Color DEFAULT_LEAF_COLOR = new Color(50, 200, 30);
    private static final float WIND_BLOW_TRANSITION_START = 0f;
    private static final float WIND_BLOW_TRANSITION_END = 6f;
    private static final float WIND_BLOW_TRANSITION_DURATION = 1f;
    private static final float WIND_BLOW_TRANSITION_ANGLE_MULTIPLIER = 2f;
    private static final float WIND_BLOW_TRANSITION_X_SIZE_MULTIPLIER = 0.5f;
    private static final boolean WIND_BLOW_SCHEDULE_REPEAT = false;
    private static final int BLOCK_SIZE = 30; // TODO move
    private static final Vector2 DEFAULT_LEAF_SIZE = Vector2.ONES.mult(BLOCK_SIZE);
    private final Supplier<Integer> getAvatarJumpCount;

    private int jumpCounter = 0;
    private float jumpTransitionAngle = 0;
    private static final float JUMP_ANGLE_TRANSITION_DELTA = 90f;
    private static final float JUMP_ANGLE_TRANSITION_DURATION = 1f;

    private static final Random random = new Random(); // TODO move to global

    public Leaf(Vector2 topLeftCorner, Supplier<Integer> getAvatarJumpCount) {
        super(topLeftCorner, DEFAULT_LEAF_SIZE,
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_LEAF_COLOR)));
        this.getAvatarJumpCount = getAvatarJumpCount;
        GameObject leaf = this;
        new ScheduledTask(this, random.nextFloat(), WIND_BLOW_SCHEDULE_REPEAT,
                () -> new Transition<Float>(leaf, this::leafTransitionEffects,
                        WIND_BLOW_TRANSITION_START, WIND_BLOW_TRANSITION_END,
                        Transition.LINEAR_INTERPOLATOR_FLOAT, WIND_BLOW_TRANSITION_DURATION,
                        Transition.TransitionType.TRANSITION_LOOP, null));
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    private void leafTransitionEffects(float value) {
        renderer().setRenderableAngle(value * WIND_BLOW_TRANSITION_ANGLE_MULTIPLIER);
        float newXSize = DEFAULT_LEAF_SIZE.x() - value * WIND_BLOW_TRANSITION_X_SIZE_MULTIPLIER;
        setDimensions(new Vector2(newXSize, DEFAULT_LEAF_SIZE.y()));
    }

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
        }
    }
}
