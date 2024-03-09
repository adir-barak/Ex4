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
 * Trunk class generates and manages the trunk of tree in the game world.
 */
public class Trunk extends GameObject {
    // The color of the trunk of tree.
    private static final Color DEFAULT_COLOR = new Color(100, 50, 20);

    private static final Vector2 DEFAULT_SIZE = new Vector2(TRUNK_SIZE_X, TRUNK_SIZE_Y);
    private final Supplier<Integer> getAvatarJumpCount;
    private int jumpCounter = 0;

    public Trunk(Vector2 topLeftCorner, Supplier<Integer> countForEventTrigger) {
        super(topLeftCorner, DEFAULT_SIZE,
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_COLOR)));
        this.getAvatarJumpCount = countForEventTrigger;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TRUNK_TAG);
    }

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
