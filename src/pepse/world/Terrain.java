package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Block;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static pepse.main.PepseConstants.*;

/**
 * Terrain class generates and manages the terrain in the game world.
 */
public class Terrain {
    // The factor for terrain generation.
    private static final float NOISE_FACTOR = 7;
    // The base color of the ground.
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    // The depth of the terrain
    private static final int TERRAIN_DEPTH = 20;

    private final int groundHeightAtX0;
    NoiseGenerator noiseGenerator;
    Vector2 windowDimensions;

    /**
     * Constructs a terrain object with the given window dimensions and seed.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for terrain generation.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = (int)(TERRAIN_START_PERCENT * windowDimensions.y());
        this.noiseGenerator = new NoiseGenerator(seed,groundHeightAtX0);
        this.windowDimensions = windowDimensions;
    }

    /**
     * Calculates the ground height at the given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the given x-coordinate.
     */
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, BLOCK_SIZE * NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates a list of blocks representing the terrain within the specified range.
     *
     * @param minX The minimum x-coordinate of the terrain range.
     * @param maxX The maximum x-coordinate of the terrain range.
     * @return A list of blocks representing the terrain.
     */
    public List<Block> createInRange(int minX, int maxX){
        List<Block> list = new ArrayList<>();
        Renderable render;
        minX = normalByBlockSize(minX);
        maxX = normalByBlockSize(maxX);

        for(int i = minX; i <= maxX; i += BLOCK_SIZE) {
            float groundHeightAtI = groundHeightAt(i);
            for(int j = 0; j < TERRAIN_DEPTH; j ++) {
                render = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));//TODO Optimize the creation of new variables in memory within a loop
                Block block = new Block(new Vector2(i,j*BLOCK_SIZE + groundHeightAtI),render);
                block.setTag(GROUND_TAG);
                list.add(block);
            }
        }
        return list;
    }

    /**
     * Normalizes the given number by the block size.
     *
     * @param num The number to normalize.
     * @return The normalized number.
     */
    private int normalByBlockSize(float num){
        return (int)Math.floor((num) / BLOCK_SIZE) * BLOCK_SIZE;
    }
}
