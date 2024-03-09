package pepse.main;

import danogl.collisions.Layer;

/**
 * The PepseConstants class contains constants used throughout the Pepse game.
 * These constants include layer indices, tags, size factors, and other parameters
 * essential for game mechanics and object placement.
 *
 * @author adir.barak, asher
 */
public class PepseConstants {

    /**
     * The layer index for the sky object.
     * This is used to determine the rendering order of objects.
     */
    public static final int SKY_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for terrain objects.
     * Terrain objects are typically static and form the ground of the game world.
     */
    public static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;

    /**
     * The layer index for night object.
     * Night objects represent elements of the game visible during nighttime.
     */
    public static final int NIGHT_LAYER = Layer.UI;

    /**
     * The layer index for sun object.
     * Sun objects represent the sun in the game world.
     */
    public static final int SUN_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for sun halo object.
     * Sun halo objects represent the halo effect around the sun.
     */
    public static final int SUN_HALO_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for tree components.
     * Tree components include trunk, leaves, and fruits.
     */
    public static final int TREE_COMPONENTS_LAYER = Layer.STATIC_OBJECTS;

    /**
     * The layer index for the avatar object.
     * The avatar represents the player's character in the game.
     */
    public static final int AVATAR_LAYER = Layer.DEFAULT;

    /**
     * The layer index for cloud objects.
     * Cloud objects represent clouds in the game sky.
     */
    public static final int CLOUD_LAYER = Layer.BACKGROUND;


    /**
     * The tag for ground objects.
     * Ground objects are typically part of the terrain.
     */
    public static final String GROUND_TAG = "ground";

    /**
     * The tag for sky object.
     * Sky objects represent elements of the game visible in the sky.
     */
    public static final String SKY_TAG = "sky";

    /**
     * The tag for trunk of tree object.
     * Trunk objects represent the main trunk of a tree.
     */
    public static final String TRUNK_TAG = "trunk";

    /**
     * The tag for block objects.
     * Block objects are typically used for building structures.
     */
    public static final String BLOCK_TAG = "block";

    /**
     * The tag for night object.
     * Night objects represent elements of the game visible during nighttime.
     */
    public static final String NIGHT_TAG = "night";

    /**
     * The tag for sun object.
     * Sun objects represent the sun in the game world.
     */
    public static final String SUN_TAG = "sun";

    /**
     * The tag for sun halo object.
     * Sun halo objects represent the halo effect around the sun.
     */
    public static final String SUN_HALO_TAG = "sunHalo";

    /**
     * The tag for cloud object.
     * Cloud objects represent clouds in the game sky.
     */
    public static final String CLOUD_TAG = "cloud";

    /**
     * The tag for avatar object.
     * The avatar represents the player's character in the game.
     */
    public static final String AVATAR_TAG = "avatar";

    /**
     * The tag for fruit objects.
     * Fruit objects represent fruits that can be collected in the game.
     */
    public static final String FRUIT_TAG = "fruit";


    /**
     * The percentage of the window height where terrain generation starts.
     * This constant determines at what height the terrain generation begins,
     * relative to the window height.
     */
    public static final float TERRAIN_START_PERCENT = 2 / 3f;

    /**
     * A constant representing half.
     * This constant represents the value of half, used in calculations.
     */
    public static final float FACTOR_HALF = 0.5f;

    /**
     * SunHalo size factor relative to the sun.
     * This constant determines the size factor of the sun halo relative to the sun size.
     */
    public static final float FACTOR_HALO_SUN = 2f;

    /**
     * Cloud height factor.
     * This constant represents the y-axis offset factor of clouds.
     */
    public static final float CLOUD_Y_OFFSET_FACTOR = 0.1f;

    /**
     * The size of the block.
     * This constant represents the size of blocks used in the game.
     */
    public static final int BLOCK_SIZE = 30;

    /**
     * The cycle length for game events.
     * This constant represents the length of a cycle for game events.
     */
    public static final float CYCLE_LENGTH = 30f;

    /**
     * The size of the leaf.
     * This constant represents the size of leaves used in the game.
     */
    public static final int LEAF_SIZE = BLOCK_SIZE;

    /**
     * The size of the fruit.
     * This constant represents the size of fruits used in the game.
     */
    public static final float FRUIT_SIZE = LEAF_SIZE * 0.9f;

    /**
     * The width of the tree trunk.
     * This constant represents the width of the tree trunk.
     */
    public static final int TRUNK_SIZE_X = BLOCK_SIZE;

    /**
     * The height of the tree trunk.
     * This constant represents the height of the tree trunk.
     */
    public static final int TRUNK_SIZE_Y = 6 * BLOCK_SIZE;
}
