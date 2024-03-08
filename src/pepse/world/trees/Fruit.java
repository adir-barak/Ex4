package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Fruit extends GameObject {
    private static final Color[] FRUIT_COLORS = new Color[]{
            new Color(200, 42, 69), new Color(69, 42, 200)
    };
    private static final int DEFAULT_COLOR_IND = 0;
    private final Supplier<Integer> getAvatarJumpCount;
    private final Consumer<Float> changeAvatarEnergyBy;

    private int currentColorIndex = 0;
    private int jumpCounter = 0;

    private static final float ENERGY_GAIN = 10f;

    // TODO move this
    private static final int BLOCK_SIZE = 30;
    private static final Vector2 DEFAULT_FRUIT_SIZE = Vector2.ONES.mult(BLOCK_SIZE).mult(0.9f);

    private boolean onCooldown = false;
    private static final String DEFAULT_TAG = "fruit";

    // TODO move to constants (pull from global)
    private static final float CYCLE_DURATION = 30f;

    public Fruit(Vector2 topLeftCorner, Supplier<Integer> getAvatarJumpCount,
                 Consumer<Float> changeAvatarEnergyBy) {
        super(topLeftCorner, DEFAULT_FRUIT_SIZE, new OvalRenderable((FRUIT_COLORS[DEFAULT_COLOR_IND])));
        this.getAvatarJumpCount = getAvatarJumpCount;
        this.changeAvatarEnergyBy = changeAvatarEnergyBy;
        setTag(DEFAULT_TAG); // TODO move to constants
    }

    private void goOnCooldown() {
        onCooldown = true;
        renderer().setRenderable(null);
        new ScheduledTask(this, CYCLE_DURATION, false,
                () -> {
                    onCooldown = false;
                    renderer().setRenderable(new OvalRenderable(FRUIT_COLORS[currentColorIndex]));
                });
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !onCooldown && super.shouldCollideWith(other);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // TODO move this constant
        if (other.getTag().equals("avatar")) {
            changeAvatarEnergyBy.accept(ENERGY_GAIN);
            goOnCooldown();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int newJumpCount = getAvatarJumpCount.get();
        if (jumpCounter != newJumpCount) {
            jumpCounter = newJumpCount;
            currentColorIndex = 1 - currentColorIndex; // transition between 0 and 1
            if (!onCooldown) {
                renderer().setRenderable(new OvalRenderable(FRUIT_COLORS[currentColorIndex]));
            }

        }
    }
}
