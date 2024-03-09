package pepse.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.ui.EnergyLevelPercentageUI;
import pepse.world.Avatar;
import pepse.world.background.Cloud;
import pepse.world.background.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import static pepse.main.PepseConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The PepseGameManager class extends GameManager and initializes the Pepse game.
 * It sets up game objects, input listeners, and window controllers.
 *
 * @author adir.barak, asher
 */
public class PepseGameManager extends GameManager {
    /**
     * A random number generator used throughout the game.
     */
    public static final Random random = new Random();

    // Seed for generating terrain
    private static final int SEED = 0;

    // Supplier for getting the avatar's jump count
    private Supplier<Integer> getAvatarJumpCount;
    // Consumer for changing the avatar's energy level
    private Consumer<Float> changeAvatarEnergyBy;
    // Supplier for getting the current energy level percentage
    private Supplier<Float> getCurrentEnergyLevelPercentage;
    // Function for getting the ground height at a given x-coordinate
    private Function<Float, Float> getGroundHeightAt;

    private Flora flora;
    private static final Float AVATAR_START_X_POS = 0f;
    private boolean thereIsATreeOnAvatarStartingPosX = false;
    private int mapMinX;
    private int mapMaxX;
    private Avatar avatar;

    PepseGameManager() {
        super();
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
        mapMinX = 0;
        mapMaxX = (int) windowController.getWindowDimensions().x();
        avatar = new Avatar(Vector2.ZERO, inputListener,
                imageReader);
        getAvatarJumpCount = avatar::getJumpCounter;
        changeAvatarEnergyBy = avatar::changeEnergyBy;
        getCurrentEnergyLevelPercentage = avatar::getCurrentEnergyLevelPercentage;
        Vector2 windowDims = windowController.getWindowDimensions();
        createMap(windowDims, imageReader);
        initAvatar();
        initEnergyUI();
    }

    /**
     * Initializes the avatar and adds it to the game.
     */
    private void initAvatar() {
        float avatarStartPlatformHeight = getGroundHeightAt.apply(AVATAR_START_X_POS);
        if (thereIsATreeOnAvatarStartingPosX) {
            avatarStartPlatformHeight -= TRUNK_SIZE_Y;
        }
        avatar.positionOnGroundAtHeight(AVATAR_START_X_POS, avatarStartPlatformHeight);
        gameObjects().addGameObject(avatar, AVATAR_LAYER);
    }

    /**
     * Initializes the energy level UI and adds it to the game.
     */
    private void initEnergyUI() {
        gameObjects().addGameObject(new EnergyLevelPercentageUI(getCurrentEnergyLevelPercentage));
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
     * Creates the game map by adding various game objects such as sky, terrain, night, sun, clouds, and trees.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param imageReader      The image reader for loading images.
     */
    private void createMap(Vector2 windowDimensions, ImageReader imageReader) {
        // Add sky object to the game
        createSky(windowDimensions);
        //create terrain
        createTerrain(windowDimensions);
        //create night
        createNight(windowDimensions);
        //create sun
        createSunWithHalo(windowDimensions);
        //create sun
        createCloud(windowDimensions, imageReader);
        //create Trees
        flora = new Flora(getGroundHeightAt, getAvatarJumpCount, changeAvatarEnergyBy);
        createForest(windowDimensions);
    }

    /**
     * Creates a forest within the specified range and adds tree components to the game.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void createForest(Vector2 windowDimensions) {
        // create a forest in the range of the whole screen
        for (ArrayList<GameObject> treeComponents : flora.createInRange(mapMinX, mapMaxX)) {
            for (GameObject treeComponent : treeComponents) {
                // Check if the tree component intersects with the avatar's starting position
                if (treeComponent.getTag().equals(TRUNK_TAG)) {
                    float treeStartX = treeComponent.getTopLeftCorner().x();
                    float treeEndX = treeStartX + treeComponent.getDimensions().x();
                    if (treeStartX <= AVATAR_START_X_POS && AVATAR_START_X_POS < treeEndX) {
                        thereIsATreeOnAvatarStartingPosX = true;
                    }
                }
                // Add the tree component to the game objects
                gameObjects().addGameObject(treeComponent, TREE_COMPONENTS_LAYER);
            }
        }
    }

    /**
     * Creates the sky objects and adds them to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createSky(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, SKY_LAYER);
    }

    /**
     * Creates the terrain and adds it to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, SEED);
        getGroundHeightAt = terrain::groundHeightAt;
        List<Block> list = terrain.createInRange(mapMinX, mapMaxX);
        for (Block block : list) {
            gameObjects().addGameObject(block, TERRAIN_LAYER);
        }
    }

    /**
     * Creates the night object and adds it to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createNight(Vector2 windowDimensions) {
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);
    }

    /**
     * Creates the sun and sun halo objects and adds them to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     */
    private void createSunWithHalo(Vector2 windowDimensions) {
        // Create sun
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        // Create sunHalo relative to sun
        GameObject sunHalo = SunHalo.create(sun);
        // Add sunHalo before the sun
        gameObjects().addGameObject(sunHalo, SUN_HALO_LAYER);
        gameObjects().addGameObject(sun, SUN_LAYER);
    }

    /**
     * Creates the sun and sun halo objects and adds them to the game objects.
     *
     * @param windowDimensions The window windowDimensions of the game.
     * @param imageReader      The renderable representing the object.
     */
    private void createCloud(Vector2 windowDimensions, ImageReader imageReader) {
        //Create cloud
        GameObject cloud = Cloud.create(windowDimensions, CYCLE_LENGTH, imageReader);
        gameObjects().addGameObject(cloud, CLOUD_LAYER);
    }
}
