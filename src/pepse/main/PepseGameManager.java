package pepse.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.Block;
import pepse.ui.EnergyLevelPercentageUI;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Trunk;

import javax.swing.*;

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

    PepseGameManager() {
        super("asd", new Vector2(1500, 500));
    }

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
                               UserInputListener inputListener, WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Add sky object to the game
        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), SKY_LAYER);
        //create terrain
        createTerrain(windowController);
        //create night
        createNight(windowController);
        //create sun
        createSun(windowController);

        // TODO make this a clean method
        
        Avatar avatar = new Avatar(windowController.getWindowDimensions().mult(0.5f), inputListener,
                imageReader);
        gameObjects().addGameObject(avatar);
        gameObjects().addGameObject(new EnergyLevelPercentageUI(avatar::getCurrentEnergyLevelPercentage));
        createMap(windowController.getWindowDimensions());
    }

    /**
     * The main method that starts the Pepse game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }


    private void createMap(Vector2 windowDimensions)
    {
        // Add sky object to the game
        createSky(windowDimensions);
        //create terrain
        createTerrain(windowDimensions);
        //create night
        createNight(windowDimensions);
        //create sun
        createSunWithHalo(windowDimensions);
        //create Trees
        createForest();
    }

    /**
     * Creates the sky objects and adds them to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createSky(Vector2 windowDimensions)
    {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, SKY_LAYER);
    }

    /**
     * Creates the terrain and adds it to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createTerrain(WindowController windowController) {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> list = terrain.createInRange((int) Vector2.ZERO.x(),
                (int) windowController.getWindowDimensions().x());
    private void createTerrain(Vector2 windowDimensions)
    {
        Terrain terrain = new Terrain(windowDimensions,SEED);
        List<Block> list = terrain.createInRange(ZERO, (int)windowDimensions.x());
        for (Block block : list) {
            gameObjects().addGameObject(block, TERRAIN_LAYER);
        }
    }

    /**
     * Creates the night object and adds it to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createNight(Vector2 windowDimensions)
    {
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);
    }

    /**
     * Creates the sun and sun halo objects and adds them to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createSunWithHalo(Vector2 windowDimensions)
    {
        // Create sun
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        // Create sunHalo relative to sun
        GameObject sunHalo = SunHalo.create(sun);
        // Add sunHalo before the sun
        gameObjects().addGameObject(sunHalo, SUN_HALO_LAYER);
        gameObjects().addGameObject(sun, SUN_LAYER);
    }

    private void createForest() //TODO complete this function
    {
        // Create trunk
        GameObject trunk = Trunk.create(new Vector2(500,350), new Vector2(30,150));
        gameObjects().addGameObject(trunk, TRUNK_LAYER);
    }



}
