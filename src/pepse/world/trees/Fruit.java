package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.main.PepseConstants;
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

    private static final Vector2 DEFAULT_FRUIT_SIZE = Vector2.ONES.mult(PepseConstants.FRUIT_SIZE);

    private boolean onCooldown = false;

    public Fruit(Vector2 topLeftCorner, Supplier<Integer> countForEventTrigger,
                 Consumer<Float> alterValueBy) {
        super(topLeftCorner, DEFAULT_FRUIT_SIZE, new OvalRenderable((FRUIT_COLORS[DEFAULT_COLOR_IND])));
        this.getAvatarJumpCount = countForEventTrigger;
        this.changeAvatarEnergyBy = alterValueBy;
        setTag(PepseConstants.FRUIT_TAG);
    }

    private void goOnCooldown() {
        onCooldown = true;
        renderer().setRenderable(null);
        new ScheduledTask(this, PepseConstants.CYCLE_LENGTH, false,
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
        if (other.getTag().equals(PepseConstants.AVATAR_TAG)) {
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
