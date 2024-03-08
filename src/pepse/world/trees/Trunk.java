package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import static pepse.main.ConstantsAsher.BLOCK_SIZE;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

import static pepse.main.ConstantsAsher.*;


/**
 * Trunk class generates and manages the trunk of tree in the game world.
 */
public class Trunk extends GameObject {
    // The color of the trunk of tree.
    private static final Color DEFAULT_TRUNK_COLOR = new Color(100, 50, 20);

    // TODO move from here
    private static final int BLOCK_SIZE = 30;
    private final Supplier<Integer> getAvatarJumpCount;
    private int jumpCounter = 0;

    public Trunk(Vector2 topLeftCorner, Vector2 dimensions, Supplier<Integer> getAvatarJumpCount) {
        super(topLeftCorner, dimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_TRUNK_COLOR)));
        this.getAvatarJumpCount = getAvatarJumpCount;
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
                    ColorSupplier.approximateColor(DEFAULT_TRUNK_COLOR)));
        }
    }
}
