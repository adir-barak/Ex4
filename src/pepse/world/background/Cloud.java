package pepse.world.background;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static pepse.main.PepseConstants.*;

/**
 * cloud class generates and manages the cloud in the game world.
 *
 * @author adir.barak, asher
 */
public class Cloud {
    //The size of the cloud
    private static final int SIZE = 80;

    /* credit: https://www.pinterest.com/pin/423690277450019574/ */
    private static final String CLOUD_PATH = "./assets/cloud.png";

    /**
     * Creates a game object representing the sun.
     *
     * @param windowDimension The dimensions of the game window.
     * @param cycleLength     The duration of the day-night cycle in seconds.
     * @param imageReader     The renderable representing the object.
     * @return The cloud game object.
     */
    public static GameObject create(Vector2 windowDimension, float cycleLength, ImageReader imageReader) {

        Renderable renderable = imageReader.readImage(CLOUD_PATH, true);
        Vector2 InitialCloudTopLeftPos = new Vector2(0, windowDimension.y() * CLOUD_Y_OFFSET_FACTOR);
        GameObject cloud = new GameObject(InitialCloudTopLeftPos, Vector2.ONES.mult(SIZE), renderable);
        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        cloud.setTag(CLOUD_TAG);
        float transitionStartX = InitialCloudTopLeftPos.x();
        float transitionEndX = windowDimension.x() - SIZE;

        // Add a transition for the cloud's movement
        new Transition<Float>(cloud, (Float x) -> cloud.setTopLeftCorner(new Vector2(x, cloud.getTopLeftCorner().y())),
                transitionStartX, transitionEndX, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return cloud;
    }
}
