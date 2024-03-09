package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import static pepse.main.PepseConstants.*;

/**
 * Night class creates a game object representing the night.
 *
 * @author adir.barak, asher
 */
public class Night {
    //The opacity value for objects at noon.
    private static final Float NOON_OPACITY = 0f;
    //The opacity value for objects at midnight.
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates a game object representing the night.
     *
     * @param windowDimension The dimensions of the game window.
     * @param cycleLength     The duration of the day-night cycle in seconds.
     * @return The night game object.
     */
    public static GameObject create(Vector2 windowDimension, float cycleLength) {
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));
        GameObject night = new GameObject(Vector2.ZERO, windowDimension, renderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night, night.renderer()::setOpaqueness, NOON_OPACITY, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength * FACTOR_HALF,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
