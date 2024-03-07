package pepse.main;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import static pepse.main.ConstantsAsher.*;

import java.util.List;

/**
 * The PepseGameManager class extends GameManager and initializes the Pepse game.
 * It sets up game objects, input listeners, and window controllers.
 */
public class PepseGameManager extends GameManager {

    // Seed for generating terrain
    private static final int SEED = 0;//TODO need to change form 0?
    //The duration of the day-night cycle in seconds.
    private static final int CYCLE_LENGTH = 30;

    /**
     * Initializes the game by setting up game objects, input listeners, and window controller.
     *
     * @param imageReader      The image reader for loading images.
     * @param soundReader      The sound reader for loading sounds.
     * @param inputListener    The input listener for user input.
     * @param windowController The window controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController){

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Add sky object to the game
        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), SKY_LAYER);
        //create terrain
        createTerrain(windowController);
        //create night
        createNight(windowController);
        //create sun
        createSun(windowController);
    }

    /**
     * The main method that starts the Pepse game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Creates the terrain and adds it to the game objects.
     *
     * @param windowController The window controller for managing the game window.
     */
    private void createTerrain(WindowController windowController)
    {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(),SEED);
        List<Block> list = terrain.createInRange((int)Vector2.ZERO.x(),
                (int)windowController.getWindowDimensions().x());
        for (Block block : list) {
            gameObjects().addGameObject(block, TERRAIN_LAYER);
        }
    }

    /**
     * Creates the night object and adds it to the game objects.
     *
     * @param windowController The window controller for managing the game window.
     */
    private void createNight(WindowController windowController)
    {
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);
    }

    /**
     * Creates the sun and sun halo objects and adds them to the game objects.
     *
     * @param windowController The window controller for managing the game window.
     */
    private void createSun(WindowController windowController)
    {
        // Create sun
        GameObject sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(sun, SUN_LAYER);
        // Create sunHalo relative to sun
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, SUN_HALO_LAYER);
    }
}
