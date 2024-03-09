package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.main.PepseConstants;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a fruit object in the game.
 * Fruits change color with player jumps and grant energy when collected.
 *
 * @author adir.barak, asher
 */
class Fruit extends GameObject {
    // Array of possible fruit colors
    private static final Color[] FRUIT_COLORS = new Color[]{
            new Color(200, 42, 69), new Color(69, 42, 200)
    };
    // Default color index
    private static final int DEFAULT_COLOR_IND = 0;
    private int currentColorIndex = 0;

    // Functions to interact with avatar (jump count and energy)

    private final Supplier<Integer> getAvatarJumpCount;
    private final Consumer<Float> changeAvatarEnergyBy;

    // Current color index and jump counter
    private int jumpCounter = 0;

    // Energy gain on collision and default fruit size

    private static final float ENERGY_GAIN = 10f;
    private static final Vector2 DEFAULT_FRUIT_SIZE = Vector2.ONES.mult(PepseConstants.FRUIT_SIZE);

    // Cooldown state

    private boolean onCooldown = false;

    /**
     * Creates a new Fruit object.
     *
     * @param topLeftCorner        the top left corner of the fruit's position
     * @param countForEventTrigger a function to get the avatar's jump count
     * @param alterValueBy         a function to change the avatar's energy
     */
    Fruit(Vector2 topLeftCorner, Supplier<Integer> countForEventTrigger,
          Consumer<Float> alterValueBy) {
        super(topLeftCorner, DEFAULT_FRUIT_SIZE, new OvalRenderable((FRUIT_COLORS[DEFAULT_COLOR_IND])));
        this.getAvatarJumpCount = countForEventTrigger;
        this.changeAvatarEnergyBy = alterValueBy;
        setTag(PepseConstants.FRUIT_TAG);
    }

    /**
     * Enters a cooldown state where the fruit is not visible or collidable.
     */
    private void goOnCooldown() {
        onCooldown = true;
        renderer().setRenderable(null);
        new ScheduledTask(this, PepseConstants.CYCLE_LENGTH, false,
                () -> {
                    onCooldown = false;
                    renderer().setRenderable(new OvalRenderable(FRUIT_COLORS[currentColorIndex]));
                });
    }

    /**
     * Determines if the fruit should collide with another GameObject.
     *
     * @param other the other GameObject to check for collision
     * @return true if the fruit is not on cooldown and collision is allowed, false otherwise
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !onCooldown && super.shouldCollideWith(other);
    }

    /**
     * Handles collision with another GameObject.
     * If the colliding object is the avatar, grant energy and enter cooldown.
     *
     * @param other     the GameObject that collided with this fruit
     * @param collision collision information (optional)
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(PepseConstants.AVATAR_TAG)) {
            changeAvatarEnergyBy.accept(ENERGY_GAIN);
            goOnCooldown();
        }
    }

    /**
     * Updates the fruit's state based on the avatar's jump count.
     * If the avatar's jump count has changed, switch the fruit's color and update the renderer.
     *
     * @param deltaTime the time elapsed since the last update (in seconds)
     */
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
