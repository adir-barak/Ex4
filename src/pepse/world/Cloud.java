package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.main.ConstantsAsher.*;

public class Cloud {


    public static GameObject create(Vector2 windowDimension, float cycleLength, ImageReader imageReader) {

        Renderable renderable = imageReader.readImage("./assets/cloud.png", true);
        Vector2 initialCloudCenter = new Vector2(0, windowDimension.y()*0.1f);
        GameObject cloud = new GameObject(initialCloudCenter,Vector2.ONES.mult(80), renderable);
        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        cloud.setTag("cloud");

        // Add a transition for the sun's movement around the cycle
        new Transition<Float>(cloud, (Float x) -> cloud.setCenter(new Vector2(x,cloud.getCenter().y())),
                0f, windowDimension.x(), Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return cloud;
    }
}
