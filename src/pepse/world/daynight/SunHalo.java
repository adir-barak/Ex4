package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;

import java.awt.*;

import static pepse.main.PepseConstants.*;

public class SunHalo {
    // The color of the sun halo.
    private static final Color COLOR = new Color(255, 255, 0, 20);

    /**
     * Creates a game object representing the sun halo.
     *
     * @param sun The sun game object.
     * @return The sun halo game object.
     */
    public static GameObject create(GameObject sun){
        Renderable renderable = new OvalRenderable(COLOR);
        GameObject sunHalo = new GameObject(
                sun.getCenter(), sun.getDimensions().mult(FACTOR_HALO_SUN), renderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent((float deltaTime)-> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}
