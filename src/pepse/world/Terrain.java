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

import static pepse.main.ConstantsAsher.*;

public class Terrain {
    private static final float FACTOR = 7;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    private int groundHeightAtX0; //its not final cause maybe we add infinity world
//    private final int seed;
    NoiseGenerator noiseGenerator;
    Vector2 windowDimensions;


    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = (int)(TERRAIN_START_PERCENT * windowDimensions.y());
//        this.seed = seed;
        this.noiseGenerator = new NoiseGenerator(seed,groundHeightAtX0);
        this.windowDimensions = windowDimensions;
    }

    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * FACTOR);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX){
        List<Block> list = new ArrayList<>();
        Renderable render = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        minX = normalByBlockSize(minX);
        maxX = normalByBlockSize(maxX);

        for(int i = minX; i <= maxX; i += Block.SIZE) {
            float groundHeightAtI = groundHeightAt(i);
            for(int j = 0; j < TERRAIN_DEPTH; j ++) {
                Block block = new Block(new Vector2(i,j*Block.SIZE + groundHeightAtI),render);
                block.setTag(GROUND_TAG);
                list.add(block);
            }
        }

        return list;
    }

    private int normalByBlockSize(float num){
        return (int)Math.floor((num) / Block.SIZE) * Block.SIZE;
    }
}
