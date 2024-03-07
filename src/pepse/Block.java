package pepse;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static pepse.main.ConstantsAsher.*;

/**
 * Block class represents a block game object.
 */
public class Block extends GameObject {
    /**
     * Constructs a block game object.
     *
     * @param topLeftCorner The top left corner position of the block.
     * @param renderable    The renderable component of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(BLOCK_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.setTag(BLOCK_TAG);
    }
}
