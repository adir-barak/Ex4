package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.function.Supplier;

import static pepse.main.PepseConstants.*;


/**
 * Represents the trunk of a tree in the game.
 * The trunk's color might change based on the avatar's jumps (visual effect).
 *
 * @author adir.barak, asher
 */
class Trunk extends GameObject {
    // The color of the trunk of tree.
    private static final Color DEFAULT_COLOR = new Color(100, 50, 20);

    private static final Vector2 DEFAULT_SIZE = new Vector2(TRUNK_SIZE_X, TRUNK_SIZE_Y);
    private final Supplier<Integer> getAvatarJumpCount;
    private int jumpCounter = 0;

    /**
     * Creates a new Trunk object.
     *
     * @param topLeftCorner        the top left corner of the trunk's position
     * @param countForEventTrigger a function to get the avatar's jump count
     */
    Trunk(Vector2 topLeftCorner, Supplier<Integer> countForEventTrigger) {
        super(topLeftCorner, DEFAULT_SIZE,
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_COLOR)));
        this.getAvatarJumpCount = countForEventTrigger;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TRUNK_TAG);
    }

    /**
     * Updates the trunk's appearance based on the avatar's jump count.
     * If the avatar's jump count has changed, resets the visual representation of the trunk
     * using the approximate default color.
     *
     * @param deltaTime the time elapsed since the last update (in seconds)
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int newJumpCount = getAvatarJumpCount.get();
        if (jumpCounter != newJumpCount) {
            jumpCounter = newJumpCount;
            renderer().setRenderable(new RectangleRenderable(
                    ColorSupplier.approximateColor(DEFAULT_COLOR)));
        }
    }
}
