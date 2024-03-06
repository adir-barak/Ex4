package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;

import static pepse.ConstantsAsher.*;

import java.util.List;


public class PepseGameManager extends GameManager {

    public static final int SEED = 0;///TODO need to change form 0?

    /**
     * Initializes the game by setting up game objects, input listeners, and window controller.
     *
     * @param imageReader       The image reader for loading images.
     * @param soundReader       The sound reader for loading sounds.
     * @param inputListener     The input listener for user input.
     * @param windowController  The window controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController){

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), SKY_LAYER);


        Terrain terrain = new Terrain(windowController.getWindowDimensions(),SEED);
        List<Block> list = terrain.createInRange((int)Vector2.ZERO.x(),
                                                 (int)windowController.getWindowDimensions().x());
        for (Block block : list) {
            gameObjects().addGameObject(block, TERRAIN_LAYER);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
