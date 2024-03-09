package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.main.PepseConstants.SKY_TAG;

/**
 * Sky class generates and manages the sky in the game world.
 */
public class Sky {
    // The basic color of the sky
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Creates a game object representing the sky.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return The sky game object.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject( Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
