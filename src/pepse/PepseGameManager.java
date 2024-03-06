package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Sky;


public class PepseGameManager extends GameManager {
    public static final int SKY_LAYER = Layer.BACKGROUND;

    /**
     * Initializes the game by setting up game objects, input listeners, and window controller.
     *
     * @param imageReader       The image reader for loading images.
     * @param soundReader       The sound reader for loading sounds.
     * @param inputListener     The input listener for user input.
     * @param windowController  The window controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController){
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, SKY_LAYER);

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
