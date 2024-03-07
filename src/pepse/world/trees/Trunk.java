package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import static pepse.main.ConstantsAsher.BLOCK_SIZE;
import java.awt.*;

import static pepse.main.ConstantsAsher.*;


/**
 * Trunk class generates and manages the trunk of tree in the game world.
 */
public class Trunk {
    // The color of the trunk of tree.
    private static final Color COLOR = new Color(100, 50, 0);

    /**
     * Creates a game object representing the sun.
     *
     * @param location The location of the floor on which the wood will be the trunk.
     * @param dimension The dimension of the trunk.
     * @return The trunk game object.
     */
    public static GameObject create(Vector2 location, Vector2 dimension){
        Renderable renderable = new RectangleRenderable(COLOR);
        location = location.add(new Vector2(ZERO,BLOCK_SIZE));
        GameObject trunk = new GameObject(location, dimension, renderable);
        trunk.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);//TODO leave it like that??
        trunk.setTag(TRUNK_TAG);
        return trunk;
    }

}
