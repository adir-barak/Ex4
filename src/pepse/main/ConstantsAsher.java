package pepse.main;

import danogl.collisions.Layer;

/**
 * Constants class contains constants used throughout the Pepse game.
 */
public class ConstantsAsher {

    /**
     * The layer index for sky object.
     */
    public static final int SKY_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for terrain objects.
     */
    public static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;

    /**
     * The layer index for night object.
     */
    public static final int NIGHT_LAYER = Layer.FOREGROUND;

    /**
     * The layer index for sun object.
     */
    public static final int SUN_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for sun halo object.
     */
    public static final int SUN_HALO_LAYER = Layer.BACKGROUND;

    /**
     * The layer index for sun object.
     */
    public static final int TRUNK_LAYER = Layer.STATIC_OBJECTS;


    /**
     * The tag for ground objects.
     */
    public static final String GROUND_TAG = "ground";

    /**
     * The tag for sky object.
     */
    public static final String SKY_TAG = "sky";

    /**
     * The tag for trunk of tree object.
     */
    public static final String TRUNK_TAG = "trunk";

    /**
     * The tag for block objects.
     */
    public static final String BLOCK_TAG = "block";

    /**
     * The tag for night object.
     */
    public static final String NIGHT_TAG = "night";

    /**
     * The tag for sun object.
     */
    public static final String SUN_TAG = "sun";

    /**
     * The tag for sun halo object.
     */
    public static final String SUN_HALO_TAG = "sunHalo";


    /**
     * The percentage of the window height where terrain generation starts.
     */
    public static final float TERRAIN_START_PERCENT = 2 / 3f;

    /**
     * A constant representing half.
     */
    public static final float FACTOR_HALF = 0.5f;

    /**
     * SunHalo size factor relative to the sun.
     */
    public static final float FACTOR_HALO_SUN = 2f;

    /**
     * The size of the block.
     */
    public static final int BLOCK_SIZE = 30;

    /**
     * Zero number.
     */
    public static final int ZERO = 0;
}
