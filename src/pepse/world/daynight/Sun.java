package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.main.ConstantsAsher.*;

/**
 * Sun class generates and manages the sun in the game world.
 */
public class Sun {
    //The size of the sun
    private static final int SIZE = 80;
    private static final float ARG_START_SUN = 0f;
    private static final float ARG_END_SUN = 360f;

    /**
     * Creates a game object representing the sun.
     *
     * @param windowDimension The dimensions of the game window.
     * @param cycleLength     The duration of the day-night cycle in seconds.
     * @return The sun game object.
     */
    public static GameObject create(Vector2 windowDimension, float cycleLength) {
        // Create the sun game object
        Renderable renderable = new OvalRenderable(Color.YELLOW);
        Vector2 initialSunCenter = new Vector2(windowDimension.x()*FACTOR_HALF,
                windowDimension.y()*TERRAIN_START_PERCENT*FACTOR_HALF);
        GameObject sun = new GameObject(initialSunCenter,Vector2.ONES.mult(SIZE), renderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        // Add a transition for the sun's movement around the cycle
        Vector2 cycle_center = new Vector2(windowDimension.x()*FACTOR_HALF,
                windowDimension.y()*TERRAIN_START_PERCENT);
        new Transition<Float>(sun, (Float angle) ->
                sun.setCenter(initialSunCenter.subtract(cycle_center).rotated(angle).add(cycle_center)),
                ARG_START_SUN, ARG_END_SUN, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);

        return sun;
    }
}
