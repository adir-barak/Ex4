package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import static pepse.main.ConstantsAsher.*;
import static pepse.main.ConstantsAsher.CYCLE_LENGTH;

public class Night {

    public static GameObject create(Vector2 windowDimension, float cycleLength) {
        Renderable render = new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));
        GameObject night = new GameObject(Vector2.ZERO, windowDimension, render);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night, night.renderer()::setOpaqueness, NOON_OPACITY, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, CYCLE_LENGTH/2f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
