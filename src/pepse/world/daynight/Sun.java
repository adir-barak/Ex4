package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Terrain;

import java.awt.*;
import java.util.function.Consumer;

import static pepse.main.ConstantsAsher.*;

public class Sun {


    private static final Vector2 SIZE_OF_SUN = new Vector2(80,80);



    public static GameObject create(Vector2 windowDimension, float cycleLength) {

        Renderable renderable = new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW));
        GameObject sun = new GameObject(windowDimension.mult(FACTOR_HALF),SIZE_OF_SUN, renderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 CYCLE_CENTER = new Vector2(windowDimension.x()*FACTOR_HALF, windowDimension.y()*TERRAIN_START_PERCENT);

        Vector2 initialSunCenter = new Vector2(windowDimension.x()*FACTOR_HALF, windowDimension.y()*TERRAIN_START_PERCENT*FACTOR_HALF);

        new Transition<Float>(sun,
        (Float angle) -> sun.setCenter(initialSunCenter.subtract(CYCLE_CENTER).rotated(angle).add(CYCLE_CENTER)),
        0f, 360f,
        Transition.LINEAR_INTERPOLATOR_FLOAT, CYCLE_LENGTH,
        Transition.TransitionType.TRANSITION_LOOP, null);



        return sun;
    }
}
